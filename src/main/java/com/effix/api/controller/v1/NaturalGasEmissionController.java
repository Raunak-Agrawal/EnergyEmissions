package com.effix.api.controller.v1;

import com.effix.api.dto.request.naturalgas.CreateNaturalGasMeterRequestDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.naturalgas.NaturalGasEmissionDetailResponseDTO;
import com.effix.api.dto.response.naturalgas.NaturalGasMeterResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.NaturalGasMeter;
import com.effix.api.service.NaturalGasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/natural-gas")
public class NaturalGasEmissionController {
    @Autowired
    private NaturalGasService naturalGasService;

    @PostMapping
    public ResponseEntity<ResponseDTO<NaturalGasMeterResponseDTO>> createNaturalGasMeter(@RequestBody @Valid CreateNaturalGasMeterRequestDTO naturalGasMeter) {
        NaturalGasMeter meter = naturalGasService.createNaturalGasMeter(naturalGasMeter);
        NaturalGasMeterResponseDTO responseDTO = NaturalGasMeterResponseDTO.builder()
                .id(meter.getId())
                .meterId(meter.getMeterId())
                .supplier(meter.getSupplier())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create natural gas meter success", responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<NaturalGasEmissionDetailResponseDTO>> getNaturalGasMeterDetail(@PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(ResponseDTO.success("Get natural gas meter detail success", naturalGasService.getNaturalGasMeterDetail(id)));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<NaturalGasMeterResponseDTO>>> getAllNaturalGasMeters() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Natural gas meters fetched success", naturalGasService.getAllNaturalGasMeters()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> deleteNaturalGasMeter(@PathVariable Long id) {
        naturalGasService.deleteNaturalGasMeter(id);
        return ResponseEntity.ok(ResponseDTO.success("Natural gas meter deleted successfully"));
    }
}

