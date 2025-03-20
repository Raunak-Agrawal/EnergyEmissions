package com.effix.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class RefrigerantDTO {

    private List<ApplianceDTO> appliances;
    private List<GwpDTO> gwp;
}