package com.nxtara.devoluciones.service;

import com.nxtara.devoluciones.domain.Solicitud;
import com.nxtara.devoluciones.model.CargaMasiva;
import com.nxtara.devoluciones.repository.CargaMasivaRepository;
import com.nxtara.devoluciones.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargaMasivaServiceTest {

    @Mock
    private RutValidator rutValidator;

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private CargaMasivaRepository cargaMasivaRepository;

    @InjectMocks
    private CargaMasivaService cargaMasivaService;

    @BeforeEach
    void setUp() {
        when(cargaMasivaRepository.save(any(CargaMasiva.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("Procesar CSV correctamente con fila válida")
    void procesarArchivoCsv_Exito() throws Exception {
        String contenidoCsv = "RUT;Nombre;Monto;Banco;Cuenta;Referencia\n" +
                "12345678-5;Juan Perez;150000;Banco Estado;123456789;REF-001";

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo",
                "pagos.csv",
                "text/csv",
                contenidoCsv.getBytes(StandardCharsets.UTF_8)
        );

        when(rutValidator.esValido("12345678-5")).thenReturn(true);
        when(solicitudRepository.existsByReferenciaBanco("REF-001")).thenReturn(false);

        CargaMasiva resultado = cargaMasivaService.procesarArchivoCsv(archivo);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalFilas());
        assertEquals(1, resultado.getFilasExitosas());
        assertEquals(0, resultado.getFilasFallidas());
        assertTrue(resultado.getErrores().isEmpty());

        verify(solicitudRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Detectar error por RUT inválido")
    void procesarArchivoCsv_RutInvalido() throws Exception {
        String contenidoCsv = "RUT;Nombre;Monto;Banco;Cuenta;Referencia\n" +
                "11111111-1;Maria Lopez;50000;Banco Chile;987654321;REF-002";

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo",
                "pagos.csv",
                "text/csv",
                contenidoCsv.getBytes(StandardCharsets.UTF_8)
        );

        when(rutValidator.esValido("11111111-1")).thenReturn(false);

        CargaMasiva resultado = cargaMasivaService.procesarArchivoCsv(archivo);

        assertEquals(1, resultado.getTotalFilas());
        assertEquals(0, resultado.getFilasExitosas());
        assertEquals(1, resultado.getFilasFallidas());
        assertEquals("rut_cliente", resultado.getErrores().get(0).getCampo());
    }

    @Test
    @DisplayName("Detectar error por Referencia Duplicada (Idempotencia)")
    void procesarArchivoCsv_ReferenciaDuplicada() throws Exception {
        String contenidoCsv = "RUT;Nombre;Monto;Banco;Cuenta;Referencia\n" +
                "12345678-5;Juan Perez;150000;Banco Estado;123456789;REF-001";

        MockMultipartFile archivo = new MockMultipartFile(
                "archivo",
                "pagos.csv",
                "text/csv",
                contenidoCsv.getBytes(StandardCharsets.UTF_8)
        );

        when(rutValidator.esValido("12345678-5")).thenReturn(true);
        when(solicitudRepository.existsByReferenciaBanco("REF-001")).thenReturn(true);

        CargaMasiva resultado = cargaMasivaService.procesarArchivoCsv(archivo);

        assertEquals(1, resultado.getTotalFilas());
        assertEquals(0, resultado.getFilasExitosas());
        assertEquals(1, resultado.getFilasFallidas());
        assertEquals("referencia_banco", resultado.getErrores().get(0).getCampo());
    }
}