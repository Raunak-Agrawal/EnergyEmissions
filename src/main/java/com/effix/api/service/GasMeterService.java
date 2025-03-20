package com.effix.api.service;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.GasMeter;

import java.util.List;

public interface GasMeterService {

    List<GasMeter> getGasMeters(CompanyProfile companyProfile);

    GasMeter createGasMeter(GasMeter gasMeter);

    GasMeter updateGasMeter(Long id, GasMeter gasMeter);
}

