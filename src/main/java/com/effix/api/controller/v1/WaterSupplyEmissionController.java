package com.effix.api.controller.v1;

import com.effix.api.dto.request.watersupply.CreateWaterSupplyMeterRequestDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.watersupply.WaterSupplyEmissionDetailResponseDTO;
import com.effix.api.dto.response.watersupply.WaterSupplyMeterResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.WaterSupplyMeter;
import com.effix.api.service.WaterSupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/water-supply")
public class WaterSupplyEmissionController {
    @Autowired
    private WaterSupplyService waterSupplyService;

    @PostMapping
    public ResponseEntity<ResponseDTO<WaterSupplyMeterResponseDTO>> createWaterSupplyMeter(@RequestBody @Valid CreateWaterSupplyMeterRequestDTO waterSupplyMeter) {
        WaterSupplyMeter meter = waterSupplyService.createWaterSupplyMeter(waterSupplyMeter);
        WaterSupplyMeterResponseDTO responseDTO = WaterSupplyMeterResponseDTO.builder()
                .id(meter.getId())
                .meterId(meter.getMeterId())
                .supplier(meter.getSupplier())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create water supply success", responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<WaterSupplyEmissionDetailResponseDTO>> getWaterSupplyMeterDetail(@PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(ResponseDTO.success("Get water supply meter detail success", waterSupplyService.getWaterSupplyMeterDetail(id)));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<WaterSupplyMeterResponseDTO>>> getAllNaturalGasMeters() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Water supply meters fetched success", waterSupplyService.getAllWaterSupplyMeters()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> deleteWaterSupplyMeter(@PathVariable Long id) {
        waterSupplyService.deleteWaterSupplyMeter(id);
        return ResponseEntity.ok(ResponseDTO.success("Water supply meter deleted successfully"));
    }
}

