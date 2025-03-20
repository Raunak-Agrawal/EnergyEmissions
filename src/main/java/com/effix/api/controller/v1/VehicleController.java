package com.effix.api.controller.v1;

import com.effix.api.dto.request.vehicle.CreateVehicleRequestDTO;
import com.effix.api.dto.request.vehicle.VehicleUsageRequestDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.vehicle.*;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.Vehicle;
import com.effix.api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    @GetMapping
    public ResponseEntity<ResponseDTO<List<VehicleDTO>>> getVehicles() {
        return ResponseEntity.ok(ResponseDTO.success("Get vehicles success", vehicleService.getVehicles()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<VehicleDetailResponseDTO>> getVehicleDetail(@PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(ResponseDTO.success("Get vehicle detail success", vehicleService.getVehicleDetail(id)));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<VehicleDTO>> createVehicle(@RequestBody @Valid CreateVehicleRequestDTO createVehicleRequestDTO) {
        Vehicle vehicle = vehicleService.createVehicle(createVehicleRequestDTO);
        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .id(vehicle.getId())
                .size(VehicleSizeDTO.builder().name(vehicle.getSizeCategory().name()).build())
                .model(VehicleModelDTO.builder().name(vehicle.getModel()).build())
                .fuelType(VehicleFuelTypeDTO.builder().name(vehicle.getFuelType().name()).build())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create vehicle success", vehicleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(ResponseDTO.success("Vehicle deleted successfully"));
    }

    @PutMapping("/{id}/usages")
    public ResponseEntity<ResponseDTO<?>> updateVehicleUsage(@PathVariable Long id, @RequestBody VehicleUsageRequestDTO vehicleUsageRequestDTO) throws BadRequestException {
        vehicleService.addVehicleUsage(id, vehicleUsageRequestDTO);
        return ResponseEntity.ok(ResponseDTO.success("Vehicle usage updated successfully"));
    }
}

