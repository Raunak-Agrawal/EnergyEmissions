package com.effix.api.service.impl;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.GasMeter;
import com.effix.api.service.GasMeterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GasMeterServiceImpl implements GasMeterService {
    @Override
    public List<GasMeter> getGasMeters(CompanyProfile companyProfile) {
        return null;
    }

    @Override
    public GasMeter createGasMeter(GasMeter gasMeter) {
        return null;
    }

    @Override
    public GasMeter updateGasMeter(Long id, GasMeter gasMeter) {
        return null;
    }
}
