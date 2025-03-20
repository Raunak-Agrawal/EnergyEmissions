package com.effix.api.service;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.Payment;

public interface PaymentService {

    void savePayment(CompanyProfile companyProfile, String planId, String planInterval);

    Payment getPayment(CompanyProfile companyProfile);
}
