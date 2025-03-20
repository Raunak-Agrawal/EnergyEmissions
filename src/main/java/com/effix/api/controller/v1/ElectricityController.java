package com.effix.api.controller.v1;

import com.effix.api.dto.request.electricity.CreateElectricityMeterRequestDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.electricity.ElectricityEmissionDetailResponseDTO;
import com.effix.api.dto.response.electricity.ElectricityMeterResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.ElectricityMeter;
import com.effix.api.service.ElectricityService;
import com.effix.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/electricity-meters")
public class ElectricityController {

    @Autowired
    ElectricityService electricityService;

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDTO<ElectricityMeterResponseDTO>> createElectricityMeter(@RequestBody @Valid CreateElectricityMeterRequestDTO electricityMeter) {
        ElectricityMeter meter = electricityService.createElectricityMeter(electricityMeter);
        ElectricityMeterResponseDTO responseDTO = ElectricityMeterResponseDTO.builder()
                .id(meter.getId())
                .meterId(meter.getMeterId())
                .supplier(meter.getSupplier())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create electricity meter success", responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ElectricityEmissionDetailResponseDTO>> getElectricityMeterDetailDetail(@PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(ResponseDTO.success("Get electricity meter detail success", electricityService.getElectricityMeterDetail(id)));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<ElectricityMeterResponseDTO>>> getAllElectricityMeters() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Electricity meters fetched success", electricityService.getAllElectricityMeters()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> deleteElectricityMeter(@PathVariable Long id) {
        electricityService.deleteElectricityMeter(id);
        return ResponseEntity.ok(ResponseDTO.success("Electricity meter deleted successfully"));
    }

}
