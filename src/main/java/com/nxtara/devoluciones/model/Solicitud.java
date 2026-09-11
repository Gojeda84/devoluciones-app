package com.nxtara.devoluciones.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_devolucion", nullable = false, unique = true, length = 20)
    private String codigoDevolucion;

    @Column(name = "cliente_rut", nullable = false, length = 12)
    private String clienteRut;

    @Column(name = "monto_devolucion", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoDevolucion;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = "CREADA";
        }
    }
}