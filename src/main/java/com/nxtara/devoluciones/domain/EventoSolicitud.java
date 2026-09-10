package com.nxtara.devoluciones.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento_solicitud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private Solicitud solicitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_origen", length = 20)
    private EstadoSolicitud estadoOrigen;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_destino", nullable = false, length = 20)
    private EstadoSolicitud estadoDestino;

    @Column(nullable = false, length = 50)
    private String usuario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }
}