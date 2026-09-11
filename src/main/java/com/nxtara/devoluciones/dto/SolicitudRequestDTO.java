package com.nxtara.devoluciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class SolicitudRequestDTO {
    @NotBlank
    private String codigoDevolucion;

    @NotBlank
    private String clienteRut;

    @NotNull
    @Positive
    private BigDecimal montoDevolucion;

    @NotBlank
    private String usuarioCreacion;
}