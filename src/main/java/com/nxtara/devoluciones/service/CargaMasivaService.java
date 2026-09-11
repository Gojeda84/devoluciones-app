package com.nxtara.devoluciones.service;

import com.nxtara.devoluciones.domain.Solicitud;
import com.nxtara.devoluciones.domain.EstadoSolicitud;
import com.nxtara.devoluciones.model.CargaError;
import com.nxtara.devoluciones.model.CargaMasiva;
import com.nxtara.devoluciones.repository.CargaMasivaRepository;
import com.nxtara.devoluciones.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CargaMasivaService {

    private final RutValidator rutValidator;
    private final SolicitudRepository solicitudRepository;
    private final CargaMasivaRepository cargaMasivaRepository;

    public CargaMasivaService(RutValidator rutValidator, 
                               SolicitudRepository solicitudRepository, 
                               CargaMasivaRepository cargaMasivaRepository) {
        this.rutValidator = rutValidator;
        this.solicitudRepository = solicitudRepository;
        this.cargaMasivaRepository = cargaMasivaRepository;
    }

    @Transactional
    public CargaMasiva procesarArchivoCsv(MultipartFile archivo) throws Exception {
        CargaMasiva carga = new CargaMasiva();
        carga.setNombreArchivo(archivo.getOriginalFilename());

        List<Solicitud> solicitudesAInsertar = new ArrayList<>();
        List<CargaError> errores = new ArrayList<>();
        int totalFilas = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            boolean esCabecera = true;

            while ((linea = br.readLine()) != null) {
                if (esCabecera) {
                    esCabecera = false;
                    continue;
                }
                totalFilas++;
                String[] datos = linea.split(";");

                if (datos.length < 6) {
                    errores.add(new CargaError(totalFilas, "formato", "Línea con columnas incompletas"));
                    continue;
                }

                String rut = datos[0].trim();
                String nombre = datos[1].trim();
                String montoStr = datos[2].trim();
                String banco = datos[3].trim();
                String cuenta = datos[4].trim();
                String referencia = datos[5].trim();

                // Validar RUT
                if (!rutValidator.esValido(rut)) {
                    errores.add(new CargaError(totalFilas, "rut_cliente", "RUT inválido: " + rut));
                    continue;
                }

                // Validar Monto
                BigDecimal monto;
                try {
                    monto = new BigDecimal(montoStr);
                    if (monto.compareTo(BigDecimal.ZERO) <= 0 || monto.compareTo(new BigDecimal("10000000")) > 0) {
                        errores.add(new CargaError(totalFilas, "monto", "Monto fuera de rango (0 - 10.000.000)"));
                        continue;
                    }
                } catch (Exception e) {
                    errores.add(new CargaError(totalFilas, "monto", "Monto no numérico"));
                    continue;
                }

                // Idempotencia
                if (solicitudRepository.existsByReferenciaBanco(referencia)) {
                    errores.add(new CargaError(totalFilas, "referencia_banco", "Referencia duplicada: " + referencia));
                    continue;
                }

                // Construcción de la entidad usando el Builder de Lombok
                Solicitud solicitud = Solicitud.builder()
                        .folio("DEV-2026-" + String.format("%06d", totalFilas) + "-" + UUID.randomUUID().toString().substring(0, 4))
                        .rutCliente(rut)
                        .nombreCliente(nombre)
                        .monto(monto)
                        .moneda("CLP")
                        .bancoDestino(banco)
                        .cuentaDestino(cuenta)
                        .referenciaBanco(referencia)
                        .origen("CARGA_MASIVA")
                        .estado(EstadoSolicitud.EN_REVISION)
                        .creadaPor("SISTEMA_MASIVO")
                        .actualizadaPor("SISTEMA_MASIVO")
                        .reabiertaCount(0)
                        .build();

                solicitudesAInsertar.add(solicitud);
            }
        }

        solicitudRepository.saveAll(solicitudesAInsertar);

        carga.setTotalFilas(totalFilas);
        carga.setFilasExitosas(solicitudesAInsertar.size());
        carga.setFilasFallidas(errores.size());
        carga.setErrores(errores);

        return cargaMasivaRepository.save(carga);
    }
}