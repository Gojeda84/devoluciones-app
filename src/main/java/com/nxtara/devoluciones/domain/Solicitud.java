package com.nxtara.devoluciones.domain;

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

    @Column(unique = true, nullable = false, length = 20)
    private String folio;

    @Column(name = "rut_cliente", nullable = false, length = 12)
    private String rutCliente;

    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Builder.Default
    @Column(nullable = false, length = 3)
    private String moneda = "CLP";

    @Column(name = "banco_destino", nullable = false, length = 50)
    private String bancoDestino;

    @Column(name = "cuenta_destino", nullable = false, length = 30)
    private String cuentaDestino;

    @Column(name = "referencia_banco", unique = true, length = 50)
    private String referenciaBanco;

    @Column(nullable = false, length = 20)
    private String origen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSolicitud estado;

    @Column(name = "motivo_rechazo", columnDefinition = "TEXT")
    private String motivoRechazo;

    @Builder.Default
    @Column(name = "reabierta_count", nullable = false)
    private Integer reabiertaCount = 0;

    @Column(name = "creada_por", nullable = false, length = 50)
    private String creadaPor;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "actualizada_por", nullable = false, length = 50)
    private String actualizadaPor;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}