package com.effix.api.controller.v1;

import com.effix.api.dto.request.profile.CreateCompanyProfileDTO;
import com.effix.api.dto.request.profile.CreatePlanPaymentDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.profile.CompanyData;
import com.effix.api.dto.response.profile.CreateCompanyProfileResponseDTO;
import com.effix.api.dto.response.profile.CreatePlanPaymentResponseDTO;
import com.effix.api.enums.Country;
import com.effix.api.enums.PaymentInterval;
import com.effix.api.enums.PaymentPlan;
import com.effix.api.exception.*;
import com.effix.api.mappers.CompanyProfileMapper;
import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.UserService;
import com.effix.api.service.impl.MailContentBuilder;
import com.effix.api.service.impl.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/profiles")
@Slf4j
public class CompanyProfileController {

    @Autowired
    CompanyProfileService companyProfileService;

    @Autowired
    private CompanyProfileMapper companyProfileMapper;

    @Autowired
    UserService userService;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private MailService mailService;

    @GetMapping
    public ResponseEntity<ResponseDTO<CreateCompanyProfileResponseDTO>> getCompanyProfile() throws NotFoundException {
        CompanyProfile companyProfile = companyProfileService.getCompanyProfile();
        if(companyProfile == null){
            throw new NotFoundException("company profile not found");
        }
        CreateCompanyProfileResponseDTO companyProfileResponseDTO = companyProfileMapper.companyProfileEntityToCreateCompanyProfileResponseDTO(companyProfile);
        return ResponseEntity.ok(ResponseDTO.success("Company profile fetched successfully", companyProfileResponseDTO));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<CreateCompanyProfileResponseDTO>> createCompanyProfile(@RequestBody @Valid CreateCompanyProfileDTO companyProfileDTO) throws EntityExistsException {
        CompanyProfile companyProfile = companyProfileService.createCompanyProfile(companyProfileDTO);
        CreateCompanyProfileResponseDTO companyProfileResponseDTO = companyProfileMapper.companyProfileEntityToCreateCompanyProfileResponseDTO(companyProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Company profile created successfully", companyProfileResponseDTO));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO<CreateCompanyProfileResponseDTO>> updateUserProfile(@RequestBody @Valid CreateCompanyProfileDTO companyProfileDTO) throws NotFoundException {
        CompanyProfile companyProfile = companyProfileService.updateCompanyProfile(companyProfileDTO);
        CreateCompanyProfileResponseDTO companyProfileResponseDTO = companyProfileMapper.companyProfileEntityToCreateCompanyProfileResponseDTO(companyProfile);
        return ResponseEntity.ok(ResponseDTO.success("Company profile updated successfully", companyProfileResponseDTO));
    }

    @PostMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<CreateCompanyProfileResponseDTO>> uploadLogo(@RequestPart(value = "file") MultipartFile file) throws NotFoundException, IOException, BadRequestException {
        CompanyProfile companyProfile = companyProfileService.uploadLogo(file);
        CreateCompanyProfileResponseDTO companyProfileResponseDTO = companyProfileMapper.companyProfileEntityToCreateCompanyProfileResponseDTO(companyProfile);
        return ResponseEntity.ok(ResponseDTO.success("Company logo updated successfully", companyProfileResponseDTO));
    }

    @DeleteMapping("/logo")
    public ResponseEntity<ResponseDTO<?>> deleteLogo() throws NotFoundException {
        companyProfileService.deleteLogo();
        return ResponseEntity.ok(ResponseDTO.success("Company logo deleted successfully"));
    }

    @GetMapping("/companies-data")
    public ResponseEntity<ResponseDTO<List<CompanyData>>> getCompaniesDetails(@RequestParam String countryName, @RequestParam String prefix) throws BadRequestException, ServerException {
        Country country = null;
        if(StringUtils.isEmpty(countryName) || StringUtils.isEmpty(prefix)){
            throw new BadRequestException("required parameters missing in the request");
        }
        try {
            country = Country.valueOf(countryName.toUpperCase());
        }
        catch (IllegalArgumentException e){
            throw new BadRequestException("country name is not valid");
        }
        List<CompanyData> results = companyProfileService.getCompaniesData(country, prefix);
        return ResponseEntity.ok(ResponseDTO.success("Company data fetched successfully", results)) ;
    }

    @PostMapping("/payment")
    public ResponseEntity<ResponseDTO<?>> makePayment(@RequestBody @Valid CreatePlanPaymentDTO createPlanPaymentDTO) throws ServerException, BadRequestException {
        PaymentPlan paymentPlan = null;
        PaymentInterval paymentInterval = null;

        try {
            paymentInterval = PaymentInterval.valueOf(createPlanPaymentDTO.getPaymentInterval().toUpperCase());
            paymentPlan = PaymentPlan.valueOf(createPlanPaymentDTO.getPaymentPlan().toUpperCase());
        }
        catch (IllegalArgumentException e){
            throw new BadRequestException("request parameters are not valid");
        }

        if (PaymentPlan.EXCLUSIVE.getPlan().equalsIgnoreCase(paymentPlan.getPlan())) {
            CompanyProfile companyProfile = companyProfileService.getCompanyProfile();

            Map<String, Object> variables = new HashMap<>();
            variables.put("userEmail", companyProfile.getUserModel().getEmail());
            variables.put("companyName", companyProfile.getCompanyName());
            variables.put("country", companyProfile.getCountry());
            variables.put("planInterval", paymentInterval.getInterval());

            String message = mailContentBuilder.generateMailContent(variables, "exclusivePaymentPlanTemplate");
            mailService.sendMail("support@accounting.bi", "sales@geteffix.com", "Exclusive plan support", message, message);
            return ResponseEntity.ok(ResponseDTO.success("payment support request sent successfully"));
        } else {
            CreatePlanPaymentResponseDTO paymentResponseDTO = companyProfileService.makePayment(paymentInterval.getInterval(), paymentPlan.getPlan());
            return ResponseEntity.ok(ResponseDTO.success("payment initiated successfully", paymentResponseDTO));
        }
    }

    @PostMapping("/payment/webhook")
    public ResponseEntity<ResponseDTO<?>> receiveStripeWebhook(@RequestBody String json, HttpServletRequest httpRequest) throws BadRequestException {
        try {
            companyProfileService.handlePaymentWebhook(json, httpRequest);
        } catch (JsonProcessingException | ServerException e) {
            ResponseEntity.internalServerError().body(ResponseDTO.failure("stripe webhook response parsing failed"));
        }
        return ResponseEntity.ok(ResponseDTO.success("payment webhook handled successfully"));
    }
}

