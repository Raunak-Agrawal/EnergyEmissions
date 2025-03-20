package com.effix.api.dto;

import com.effix.api.model.entity.Lookup;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TransportDTO {

    private List<Lookup> models;
    private List<Lookup> sizes;
    private List<Lookup> fuel;
}
