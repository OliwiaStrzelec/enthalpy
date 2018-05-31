package com.enthalpy.model.form;

import com.enthalpy.model.Vector;
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
    private Double tempStart;
    @NotNull
    private Double tempEnd;
    @NotNull
    @Positive(message = "Wartość nie może być ujemna.")
    private Double H;
    @NotBlank
    private String function;
    private Vector vector;
}
