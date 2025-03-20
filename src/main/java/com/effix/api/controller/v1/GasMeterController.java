package com.effix.api.controller.v1;

import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.GasMeter;
import com.effix.api.service.GasMeterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gasMeter")
public class GasMeterController {

    private final GasMeterService gasMeterService;

    // Use constructor-based dependency injection
    public GasMeterController(GasMeterService gasMeterService) {
        this.gasMeterService = gasMeterService;
    }

    @GetMapping
    public List<GasMeter> getGasMeters(@RequestParam CompanyProfile companyProfile) {
        return gasMeterService.getGasMeters(companyProfile);
    }

    @PostMapping
    public GasMeter createGasMeter(@RequestBody GasMeter gasMeter) {
        return gasMeterService.createGasMeter(gasMeter);
    }

    @PutMapping("/{id}")
    public GasMeter updateGasMeter(@PathVariable Long id, @RequestBody GasMeter gasMeter) {
        return gasMeterService.updateGasMeter(id, gasMeter);
    }
}

