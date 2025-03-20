package com.effix.api.service;

import com.effix.api.dto.response.profile.CompanyData;
import com.effix.api.exception.ServerException;

import java.util.List;

public interface CompanyDataService {
    String getCountry();
    List<CompanyData> getCompanyData(String prefix) throws ServerException;
}
