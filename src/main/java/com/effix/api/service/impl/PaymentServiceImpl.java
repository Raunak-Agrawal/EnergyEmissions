package com.effix.api.service.impl;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.Payment;
import com.effix.api.repository.PaymentRepository;
import com.effix.api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Override
    public void savePayment(CompanyProfile companyProfile, String planId, String planInterval) {
        Payment payment = Payment.builder()
                .companyProfile(companyProfile)
                .plan(planId)
                .interval(planInterval)
                .build();
        paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(CompanyProfile companyProfile) {
        return paymentRepository.findByCompanyProfile(companyProfile);
    }
}
