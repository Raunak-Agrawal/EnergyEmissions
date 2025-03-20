package com.effix.api.service.impl;

import com.effix.api.dto.request.profile.CreateCompanyProfileDTO;
import com.effix.api.dto.response.profile.CompanyData;
import com.effix.api.dto.response.profile.CreatePlanPaymentResponseDTO;
import com.effix.api.dto.response.profile.StripeWebhookResponse;
import com.effix.api.enums.Country;
import com.effix.api.enums.PaymentInterval;
import com.effix.api.exception.*;
import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.UserModel;
import com.effix.api.repository.CompanyProfileRepository;
import com.effix.api.service.CompanyDataService;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.PaymentService;
import com.effix.api.service.UserService;
import com.effix.api.util.CompanyDataServiceFactory;
import com.effix.api.util.PaymentUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CompanyProfileServiceImpl implements CompanyProfileService {

    @Autowired
    CompanyProfileRepository companyProfileRepository;

    @Autowired
    PaymentService paymentService;

    @Autowired
    UserService userService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${frontend-domain}")
    private String frontendDomain;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyDataServiceFactory companyDataServiceFactory;

    @Override
    public CompanyProfile getCompanyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        UserModel userModel = userService.findUserById(Long.parseLong(userId));
        return companyProfileRepository.findByUserModel(userModel);
    }

    private UserModel getUserModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        return userService.findUserById(Long.parseLong(userId));
    }

    @Override
    public CompanyProfile getCompanyProfile(UserModel userModel) {
        return companyProfileRepository.findByUserModel(userModel);
    }

    @Override
    public CompanyProfile createCompanyProfile(CreateCompanyProfileDTO companyProfileDTO) throws EntityExistsException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        UserModel userModel = userService.findUserById(Long.parseLong(userId));

        CompanyProfile companyProfile = companyProfileRepository.findByUserModel(userModel);
        if(companyProfile == null) {
            companyProfile = CompanyProfile.builder()
                    .userModel(userModel)
                    .jobTitle(companyProfileDTO.getJobTitle())
                    .companyName(companyProfileDTO.getCompanyName())
                    .country(companyProfileDTO.getCountry())
                    .location(companyProfileDTO.getLocation())
                    .industry(companyProfileDTO.getIndustry())
                    .totalOfficeArea(companyProfileDTO.getTotalOfficeArea())
                    .numberOfEmployees(companyProfileDTO.getNumberOfEmployees())
                    .natureOfBusiness(companyProfileDTO.getNatureOfBusiness())
                    .build();
            return companyProfileRepository.save(companyProfile);
        }
        else throw new EntityExistsException("Company is already registered with current user's email address.");
    }

    @Override
    public CompanyProfile updateCompanyProfile(CreateCompanyProfileDTO companyProfileDTO) throws NotFoundException {
        CompanyProfile companyProfile = getCompanyProfile();
        if(companyProfile == null) {
            throw new NotFoundException("company profile not found");
        }
        companyProfile.setCompanyName(companyProfileDTO.getCompanyName());
        companyProfile.setJobTitle(companyProfileDTO.getJobTitle());
        companyProfile.setCountry(companyProfileDTO.getCountry());
        companyProfile.setIndustry(companyProfileDTO.getIndustry());
        companyProfile.setLocation(companyProfileDTO.getLocation());
        companyProfile.setTotalOfficeArea(companyProfileDTO.getTotalOfficeArea());
        companyProfile.setNatureOfBusiness(companyProfileDTO.getNatureOfBusiness());
        companyProfile.setNumberOfEmployees(companyProfileDTO.getNumberOfEmployees());

        return companyProfileRepository.save(companyProfile);
    }

    @Override
    public CompanyProfile uploadLogo(MultipartFile file) throws IOException, NotFoundException, BadRequestException {
        CompanyProfile companyProfile = getCompanyProfile();
        if(companyProfile == null){
            throw new NotFoundException("company profile not found");
        }

        if(file.isEmpty()) {
            throw new BadRequestException("file is empty");
        }

        long fileSize = file.getSize();
        if (fileSize > 1024 * 1024) { // 1MB limit
            throw new BadRequestException("File size exceeds the limit. Allowed size is 1MB");
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Only image files are allowed");
        }

        companyProfile.setLogo(file.getBytes());
        return companyProfileRepository.save(companyProfile);
    }

    @Override
    public void deleteLogo() throws NotFoundException {
        CompanyProfile companyProfile = getCompanyProfile();
        if(companyProfile == null){
            throw new NotFoundException("company profile not found");
        }
        companyProfile.setLogo(null);
        companyProfileRepository.save(companyProfile);
    }

    @Override
    public CreatePlanPaymentResponseDTO makePayment(String paymentInterval, String paymentPlan) throws ServerException {
        Stripe.apiKey = stripeApiKey;
        String clientBaseURL = frontendDomain;

        CompanyProfile companyProfile = getCompanyProfile();
        UserModel userModel = getUserModel();

        Product product = PaymentUtil.getProduct(paymentPlan);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("company_id", String.valueOf(companyProfile.getId()));
        metadata.put("plan_id", product.getId());
        metadata.put("plan_interval", paymentInterval);

        log.info("metadata for payment {}", metadata);
        // Next, create a checkout session by adding the details of the checkout
        SessionCreateParams.Builder sessionBuilder =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setCustomerEmail(userModel.getEmail())
                        .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder().putAllMetadata(metadata).build())
                        .setSuccessUrl(clientBaseURL + "/user/profile/paymentsuccess")
                        .setCancelUrl(clientBaseURL + "/user/profile/paymentfailure")
                        .addLineItem(SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(PriceData.builder()
                                        .setProductData(
                                                PriceData.ProductData.builder()
                                                        .putMetadata("product_id", product.getId())
                                                        .setName(product.getName())
                                                        .build())
                                        .setCurrency(product.getDefaultPriceObject().getCurrency())
                                        .setUnitAmountDecimal(PaymentInterval.YEARLY.getInterval().equalsIgnoreCase(paymentInterval) ? product.getDefaultPriceObject().getUnitAmountDecimal().multiply(BigDecimal.valueOf(12)) :  product.getDefaultPriceObject().getUnitAmountDecimal())
                                        .build())
                                .build());

        Session session = null;
        try {
            session = Session.create(sessionBuilder.build());
        } catch (StripeException e) {
            throw new ServerException("Error processing payment. Please try again.");
        }
        return CreatePlanPaymentResponseDTO.builder().paymentURL(session.getUrl()).build();
    }

    @Override
    public void handlePaymentWebhook(String json, HttpServletRequest httpRequest) throws BadRequestException, JsonProcessingException, ServerException {
        Event event = validateStripeHeadersAndReturnEvent(json, httpRequest.getHeader("Stripe-Signature"));
        Event.Data eventData = event.getData();

        if (eventData == null) {
            throw new BadRequestException("Unable to serialize stripe object");
        }

        if (event.getType().equals("payment_intent.succeeded")) { // Define and call a function to handle the event payment_intent.succeeded
            StripeWebhookResponse stripeWebhookResponse = objectMapper.readValue(eventData.getObject().toJson(), StripeWebhookResponse.class);

            HashMap<String, String> paymentMetadata = stripeWebhookResponse.getMetadata();
            Long companyId = Long.parseLong(paymentMetadata.get("company_id"));
            String planId = paymentMetadata.get("plan_id");
            String planInterval = paymentMetadata.get("plan_interval");

            log.info("companyId {}, planId {}, planInterval {}", companyId, planId, planInterval);

            Optional<CompanyProfile> companyProfileOptional = companyProfileRepository.findById(companyId);
            if(companyProfileOptional.isEmpty()){
                throw new BadRequestException("Unable to associate company with payment details");
            }
            try {
                paymentService.savePayment(companyProfileOptional.get(), planId, planInterval);
            }
            catch (Exception ex){
                throw new ServerException("Unable to save payment details");
            }
        }
        // not handling other types eg. charge.succeeded or payment_intent.created
    }

    private Event validateStripeHeadersAndReturnEvent(String payload, String headers) throws BadRequestException {
        try {
            return Webhook.constructEvent(payload, headers, webhookSecret);
        } catch (JsonSyntaxException e) {
            throw new BadRequestException("Invalid payload");
        } catch (SignatureVerificationException e) {
            throw new BadRequestException("Invalid Signature");
        }
    }

    @Override
    public List<CompanyData> getCompaniesData(Country country, String prefix) throws ServerException {
        CompanyDataService companyDataService = companyDataServiceFactory.getCompanyDataService(country.getCountry());
        return companyDataService.getCompanyData(prefix);
    }
}
