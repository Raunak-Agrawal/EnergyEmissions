package com.effix.api.controller.v1;

import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.service.BootStrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bootstrap")
public class BootstrapController {
    @Autowired
    private BootStrapService bootStrapService;

    @GetMapping
    public ResponseEntity<ResponseDTO<?>> getBootStrapData(@RequestParam String type) {
        if("vehicle".equalsIgnoreCase(type)) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Vehicle bootstrap data fetched successfully", bootStrapService.getVehicleBootStrapData()));
        }
        else if("electricitysuppliers".equalsIgnoreCase(type)) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Electricity suppliers bootstrap data fetched successfully", bootStrapService.getElectricitySuppliersData()));
        }
        else if("naturalgassuppliers".equalsIgnoreCase(type)) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Natural gas suppliers bootstrap data fetched successfully", bootStrapService.getNaturalGasSuppliersData()));
        }
        else if("watersupplysuppliers".equalsIgnoreCase(type)) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Water supply suppliers bootstrap data fetched successfully", bootStrapService.getWaterSupplySuppliersData()));
        }
        else if("refrigeration".equalsIgnoreCase(type)) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Refrigeration bootstrap data fetched successfully", bootStrapService.getRefrigerantBootStrapData()));
        }
        else if("employeecommuteandbusinesstravel".equalsIgnoreCase(type)){
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Employee commute and business travel data fetched successfully", bootStrapService.getEmployeeCommuteData()));
        }
        else if("wastecategories".equalsIgnoreCase(type)){
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Waste categories data fetched successfully", bootStrapService.getWasteCategories()));
        }
        return null;
    }
}
