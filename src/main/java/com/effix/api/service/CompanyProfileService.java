package com.effix.api.service;

import com.effix.api.dto.request.profile.CreateCompanyProfileDTO;
import com.effix.api.dto.response.profile.CompanyData;
import com.effix.api.dto.response.profile.CreatePlanPaymentResponseDTO;
import com.effix.api.enums.Country;
import com.effix.api.exception.*;
import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.UserModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface CompanyProfileService {

    CompanyProfile getCompanyProfile();

    CompanyProfile getCompanyProfile(UserModel userModel);

    CompanyProfile createCompanyProfile(CreateCompanyProfileDTO companyProfileDTO) throws EntityExistsException;

    CompanyProfile updateCompanyProfile(CreateCompanyProfileDTO companyProfileDTO) throws NotFoundException;

    CompanyProfile uploadLogo(MultipartFile file) throws IOException, NotFoundException, BadRequestException;

    void deleteLogo() throws NotFoundException;

    CreatePlanPaymentResponseDTO makePayment(String paymentInterval, String paymentPlan) throws ServerException;

    void handlePaymentWebhook(String json, HttpServletRequest httpRequest) throws BadRequestException, JsonProcessingException, ServerException;

    List<CompanyData> getCompaniesData(Country country, String prefix) throws ServerException;
}

