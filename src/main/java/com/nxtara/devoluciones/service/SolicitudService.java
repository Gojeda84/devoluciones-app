package com.nxtara.devoluciones.service;

import com.nxtara.devoluciones.domain.EstadoSolicitud;
import com.nxtara.devoluciones.domain.EventoSolicitud;
import com.nxtara.devoluciones.domain.Solicitud;
import com.nxtara.devoluciones.repository.EventoSolicitudRepository;
import com.nxtara.devoluciones.repository.SolicitudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final EventoSolicitudRepository eventoSolicitudRepository;

    private static final BigDecimal MONTO_MAXIMO_AUTOMATICO = new BigDecimal("100000");

    @Transactional
    public Solicitud cambiarEstado(Long id, EstadoSolicitud nuevoEstado, String usuario, String rol, String comentario) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("Solicitud no encontrada con ID: " + id));

        EstadoSolicitud estadoActual = solicitud.getEstado();

        // Validar transiciones de la máquina de estados
        validarTransicion(solicitud, nuevoEstado, rol);

        // Actualizar estado
        solicitud.setEstado(nuevoEstado);
        solicitud.setActualizadaPor(usuario);

        if (nuevoEstado == EstadoSolicitud.RECHAZADA) {
            solicitud.setMotivoRechazo(comentario);
        }

        if (estadoActual == EstadoSolicitud.RECHAZADA && nuevoEstado == EstadoSolicitud.EN_REVISION) {
            solicitud.setReabiertaCount(solicitud.getReabiertaCount() + 1);
        }

        // Registrar evento de trazabilidad
        EventoSolicitud evento = EventoSolicitud.builder()
                .solicitud(solicitud)
                .estadoOrigen(estadoActual)
                .estadoDestino(nuevoEstado)
                .usuario(usuario)
                .comentario(comentario)
                .build();

        eventoSolicitudRepository.save(evento);

        return solicitudRepository.save(solicitud);
    }

    private void validarTransicion(Solicitud solicitud, EstadoSolicitud nuevo, String rol) {
        EstadoSolicitud actual = solicitud.getEstado();

        switch (actual) {
            case BORRADOR:
                if (nuevo != EstadoSolicitud.ENVIADA) {
                    throw new ReglaNegocioException("Desde BORRADOR solo se puede pasar a ENVIADA.");
                }
                break;

            case ENVIADA:
                if (nuevo == EstadoSolicitud.APROBADA) {
                    // Evaluación de monto para aprobación automática
                    if (solicitud.getMonto().compareTo(MONTO_MAXIMO_AUTOMATICO) > 0) {
                        throw new ReglaNegocioException("Solicitudes mayores a $100.000 requieren pasar por EN_REVISION.");
                    }
                } else if (nuevo != EstadoSolicitud.EN_REVISION && nuevo != EstadoSolicitud.RECHAZADA) {
                    throw new ReglaNegocioException("Transición no permitida desde ENVIADA.");
                }
                break;

            case EN_REVISION:
                if (nuevo == EstadoSolicitud.APROBADA || nuevo == EstadoSolicitud.RECHAZADA) {
                    if (!"SUPERVISOR".equalsIgnoreCase(rol)) {
                        throw new ReglaNegocioException("Solo un SUPERVISOR puede aprobar/rechazar solicitudes en revisión.");
                    }
                } else {
                    throw new ReglaNegocioException("Transición no válida desde EN_REVISION.");
                }
                break;

            case RECHAZADA:
                if (nuevo == EstadoSolicitud.EN_REVISION) {
                    if (solicitud.getReabiertaCount() >= 1) {
                        throw new ReglaNegocioException("La solicitud ya ha sido reabierta una vez y no puede volverse a reabrir.");
                    }
                } else {
                    throw new ReglaNegocioException("Una solicitud RECHAZADA solo puede pasar a EN_REVISION.");
                }
                break;

            case APROBADA:
                if (nuevo != EstadoSolicitud.PAGADA && nuevo != EstadoSolicitud.FALLIDA) {
                    throw new ReglaNegocioException("Desde APROBADA solo se puede pasar a PAGADA o FALLIDA.");
                }
                break;

            default:
                throw new ReglaNegocioException("Estado actual no permite cambios de estado.");
        }
    }
}