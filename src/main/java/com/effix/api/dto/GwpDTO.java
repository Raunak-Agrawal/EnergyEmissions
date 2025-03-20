package com.effix.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GwpDTO{
    private String refrigerant;
    private Double emission;
}
