package com.nxtara.devoluciones.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CambioEstadoRequestDTO {
    @NotBlank
    private String nuevoEstado;

    @NotBlank
    private String usuario;

    private String comentario;
}