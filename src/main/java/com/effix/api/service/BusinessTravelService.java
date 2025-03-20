package com.effix.api.service;

import com.effix.api.dto.request.employeecommute.CreateBusinessTravelRequestDTO;
import com.effix.api.dto.request.employeecommute.CreateEmployeeCommuteRequestDTO;
import com.effix.api.dto.response.employeecommute.EmployeeCommuteEmissionResponseDTO;
import com.effix.api.exception.ServerException;
import com.effix.api.model.entity.BusinessTravel;
import com.effix.api.model.entity.EmployeeCommute;

import java.util.List;

public interface BusinessTravelService {
    BusinessTravel createBusinessTravel(CreateBusinessTravelRequestDTO businessTravelRequestDTO) throws ServerException;

    List<EmployeeCommuteEmissionResponseDTO> getAllBusinessTravelEmissions();
}
