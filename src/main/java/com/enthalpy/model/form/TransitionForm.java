package com.enthalpy.model.form;

import jdk.internal.instrumentation.TypeMapping;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class TransitionForm {
    @NotNull
    @DecimalMin(value = "56.0", message = "Wartość nie może być mniejsza niż 56.")
    private Double tempStart;
    @NotNull
    @DecimalMax(value = "1550.0", message = "Wartość nie może być większa niż 1550.")
    private Double tempEnd;
    @NotNull
    @Positive(message = "Wartość nie może być ujemna.")
    private Double H;
    @NotBlank
    private String function;
}
