package com.effix.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplianceDTO {
    private String name;
    private Double leakage;
}
