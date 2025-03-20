package com.effix.api.dto.response.profile;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCompanyProfileResponseDTO {
    private String companyName;
    private String jobTitle;
    private String location;
    private String country;
    private String industry;
    private Integer numberOfEmployees;
    private Double totalOfficeArea;
    private String natureOfBusiness;
    private byte[] logo;
}
