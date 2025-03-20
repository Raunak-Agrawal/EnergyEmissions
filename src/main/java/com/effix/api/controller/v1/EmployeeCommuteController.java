package com.effix.api.controller.v1;

import com.effix.api.dto.request.employeecommute.CreateEmployeeCommuteRequestDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.employeecommute.CreateEmployeeCommuteResponseDTO;
import com.effix.api.dto.response.employeecommute.EmployeeCommuteEmissionResponseDTO;
import com.effix.api.model.entity.EmployeeCommute;
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
@RequestMapping(value = "/api/v1/employee-commute")
public class EmployeeCommuteController {

    @Autowired
    EmployeeCommuteService employeeCommuteService;

    @PostMapping
    public ResponseEntity<ResponseDTO<CreateEmployeeCommuteResponseDTO>> createEmployeeCommute(@RequestBody @Valid CreateEmployeeCommuteRequestDTO employeeCommuteRequestDTO) {
        EmployeeCommute employeeCommute = employeeCommuteService.createEmployeeCommute(employeeCommuteRequestDTO);
        CreateEmployeeCommuteResponseDTO responseDTO = CreateEmployeeCommuteResponseDTO.builder()
                .id(employeeCommute.getId())
                .employeeName(employeeCommute.getEmployeeName())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("Create employee commute success", responseDTO));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<EmployeeCommuteEmissionResponseDTO>>> getAllEmployeeCommuteEmissions() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO.success("Employee commute emissions fetched success", employeeCommuteService.getAllEmployeeCommuteEmissions()));
    }
}
