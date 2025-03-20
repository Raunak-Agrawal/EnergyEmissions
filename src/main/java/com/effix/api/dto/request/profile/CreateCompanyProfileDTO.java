package com.effix.api.dto.request.profile;

import com.effix.api.model.entity.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateCompanyProfileDTO {
    @NotBlank(message = "company name can not be blank")
    private String companyName;
    private String jobTitle;
    private String location;
    @NotBlank(message = "country can not be blank")
    private String country;
    private String industry;
    private Integer numberOfEmployees;
    private Double totalOfficeArea;
    private String natureOfBusiness;
}

