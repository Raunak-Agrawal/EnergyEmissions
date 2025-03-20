package com.effix.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ElectricityRequestDTO {

    @NotBlank(message = "electricity_use can not be blank")
    @JsonProperty(value = "electricity_usage")
    private String electricityUse;

    @NotBlank(message = "electricity_bill can not be blank")
    @JsonProperty("electricity_bill")
    private String electricityBill;

    @NotBlank(message = "supplier can not be blank")
    @JsonProperty("supplier")
    private String supplier;

    @NotBlank(message = "electricity_source can not be blank")
    @JsonProperty("electricity_source")
    private String electricitySource;

    @NotBlank(message = "department can not be blank")
    @JsonProperty("department")
    private String department;

    @NotBlank(message = "building can not be blank")
    @JsonProperty("building")
    private String building;

    @NotNull(message = "user can not be null")
    @JsonProperty("user")
    private Long user;
}
 /*
 {
        "electricity_usage" : "2000",
        "electricity_bill" : "200",
        "supplier" : "MJHY",
        "department" : "T",
        "building" : "c",
        "electricity_source":"Solar_Panel",
        "user" : 1
}

  */