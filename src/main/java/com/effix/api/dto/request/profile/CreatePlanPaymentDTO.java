package com.effix.api.dto.request.profile;

import com.effix.api.enums.PaymentInterval;
import com.effix.api.enums.PaymentPlan;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreatePlanPaymentDTO {
    @NotNull(message = "payment plan can not be blank")
    private String paymentPlan;
    @NotNull(message = "payment interval can not be blank")
    private String paymentInterval;
}
