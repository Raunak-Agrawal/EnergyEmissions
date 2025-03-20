package com.effix.api.controller.v1;

import com.effix.api.dto.request.employeecommute.CreateEmployeeCommuteRequestDTO;
import com.effix.api.dto.request.waste.CreateWasteRequestDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.employeecommute.CreateEmployeeCommuteResponseDTO;
import com.effix.api.dto.response.employeecommute.EmployeeCommuteEmissionResponseDTO;
import com.effix.api.dto.response.waste.CreateWasteResponseDTO;
import com.effix.api.dto.response.waste.WasteEmissionResponseDTO;
import com.effix.api.model.entity.EmployeeCommute;
import com.effix.api.model.entity.UserModel;
import com.effix.api.model.entity.Waste;
import com.effix.api.service.UserService;
import com.effix.api.service.WasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/waste")
public class WasteController {
    @Autowired
    private WasteService wasteService;

    @PostMapping
    public ResponseEntity<ResponseDTO<List<CreateWasteResponseDTO>>> createWaste(@RequestBody @Valid CreateWasteRequestDTO wasteRequestDTO) {
        List<Waste> wasteList = wasteService.createWaste(wasteRequestDTO);

        List<CreateWasteResponseDTO> responseDTO = wasteList.stream().map(waste -> CreateWasteResponseDTO.builder()
                .id(waste.getId())
                .treatmentMethod(waste.getTreatmentMethod())
                .wasteCategory(waste.getWasteCategory())
                .weight(waste.getWeight())
                .disposalDate(waste.getDisposalDate())
                .build()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create waste success", responseDTO));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<WasteEmissionResponseDTO>>> getAllWasteEmissions() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Waste emissions fetched success", wasteService.getAllWasteEmissions()));
    }
}


