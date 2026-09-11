package com.nxtara.devoluciones.service;

import com.nxtara.devoluciones.domain.EstadoSolicitud;
import com.nxtara.devoluciones.domain.Solicitud;
import com.nxtara.devoluciones.repository.EventoSolicitudRepository;
import com.nxtara.devoluciones.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private EventoSolicitudRepository eventoSolicitudRepository;

    @InjectMocks
    private SolicitudService solicitudService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransicionInvalidaAnalistaApruebaSinRevision() {
        Solicitud solicitud = Solicitud.builder()
                .id(1L)
                .estado(EstadoSolicitud.ENVIADA)
                .monto(new BigDecimal("200000"))
                .build();

        when(solicitudRepository.findById(1L)).thenReturn(Optional.of(solicitud));

        assertThrows(ReglaNegocioException.class, () -> {
            solicitudService.cambiarEstado(1L, EstadoSolicitud.APROBADA, "analista1", "ANALISTA", "Aprobar");
        });
    }
}