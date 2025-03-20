package com.effix.api.mappers;

import com.effix.api.dto.response.profile.CreateCompanyProfileResponseDTO;
import com.effix.api.model.entity.CompanyProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyProfileMapper {
    CreateCompanyProfileResponseDTO companyProfileEntityToCreateCompanyProfileResponseDTO(CompanyProfile companyProfile);

}
