package com.effix.api.controller.v1;

import com.effix.api.dto.request.employeecommute.CreateBusinessTravelRequestDTO;
import com.effix.api.dto.request.employeecommute.CreateEmployeeCommuteRequestDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.employeecommute.CreateBusinessTravelResponseDTO;
import com.effix.api.dto.response.employeecommute.CreateEmployeeCommuteResponseDTO;
import com.effix.api.dto.response.employeecommute.EmployeeCommuteEmissionResponseDTO;
import com.effix.api.exception.ServerException;
import com.effix.api.model.entity.BusinessTravel;
import com.effix.api.model.entity.EmployeeCommute;
import com.effix.api.service.BusinessTravelService;
import com.effix.api.service.EmployeeCommuteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/business-travel")
public class BusinessTravelController {

    @Autowired
    BusinessTravelService businessTravelService;

    @PostMapping
    public ResponseEntity<ResponseDTO<CreateBusinessTravelResponseDTO>> createBusinessTravel(@RequestBody @Valid CreateBusinessTravelRequestDTO createBusinessTravelRequestDTO) throws ServerException {
        BusinessTravel businessTravel = businessTravelService.createBusinessTravel(createBusinessTravelRequestDTO);
        CreateBusinessTravelResponseDTO responseDTO = CreateBusinessTravelResponseDTO.builder()
                .id(businessTravel.getId())
                .groupName(businessTravel.getGroupName())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create business travel success", responseDTO));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<EmployeeCommuteEmissionResponseDTO>>> getAllBusinessTravelEmissions() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Business travel emissions fetched success", businessTravelService.getAllBusinessTravelEmissions()));
    }
}
