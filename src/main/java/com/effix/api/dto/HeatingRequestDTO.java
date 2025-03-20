package com.effix.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class HeatingRequestDTO {

    @NotBlank(message = "gas_use can not be blank")
    @JsonProperty("gas_use")
    private String gasUse;

    @NotBlank(message = "gas_bill can not be blank")
    @JsonProperty("gas_bill")
    private String gasBill;

    @NotBlank(message = "supplier can not be blank")
    @JsonProperty("supplier")
    private String supplier;

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
        "gas_use" : "100",
        "gas_bill" : "1000",
        "supplier" : "MJHY",
        "department" : "B",
        "building" : "b",
        "user" : 1
        }

 */