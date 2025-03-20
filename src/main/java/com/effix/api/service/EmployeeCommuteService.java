package com.effix.api.service;

import com.effix.api.dto.request.employeecommute.CreateEmployeeCommuteRequestDTO;
import com.effix.api.dto.response.employeecommute.EmployeeCommuteEmissionResponseDTO;
import com.effix.api.model.entity.EmployeeCommute;

import java.util.List;

public interface EmployeeCommuteService {
    EmployeeCommute createEmployeeCommute(CreateEmployeeCommuteRequestDTO employeeCommuteRequestDTO);

    List<EmployeeCommuteEmissionResponseDTO> getAllEmployeeCommuteEmissions();
}
