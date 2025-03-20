package com.effix.api.controller.v1;

import com.effix.api.dto.request.refrigerant.CreateRefrigerantRequestDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.refrigerant.RefrigerantEmissionDetailResponseDTO;
import com.effix.api.dto.response.refrigerant.RefrigerantResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.Refrigerant;
import com.effix.api.service.RefrigerationService;
import com.effix.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/refrigerations")
public class RefrigerationController {

    @Autowired
    RefrigerationService refrigerationService;

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDTO<RefrigerantResponseDTO>> createRefrigerant(@RequestBody @Valid CreateRefrigerantRequestDTO createRefrigerantRequestDTO) {
        Refrigerant refrigerant = refrigerationService.createRefrigerationRecord(createRefrigerantRequestDTO);
        RefrigerantResponseDTO responseDTO = RefrigerantResponseDTO.builder()
                .id(refrigerant.getId())
                .refrigerantCapacity(refrigerant.getRefrigerantCapacity())
                .refrigerantType(refrigerant.getRefrigerantType())
                .appliance(refrigerant.getAppliance())
                .refrigerantDisplayName(refrigerant.getName())
                .yearsOfUsage(refrigerant.getYearsOfUsage())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create refrigerant success", responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<RefrigerantEmissionDetailResponseDTO>> getRefrigerantDetail(@PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(ResponseDTO.success("Get refrigerant detail success", refrigerationService.getRefrigerantDetail(id)));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<RefrigerantResponseDTO>>> getAllRefrigerants() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Refrigerants fetched success", refrigerationService.getAllRefrigerants()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<?>> deleteRefrigerant(@PathVariable Long id) {
        refrigerationService.deleteRefrigerant(id);
        return ResponseEntity.ok(ResponseDTO.success("Refrigerant deleted successfully"));
    }
}

