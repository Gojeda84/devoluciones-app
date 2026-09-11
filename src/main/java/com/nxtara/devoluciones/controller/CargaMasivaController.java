package com.nxtara.devoluciones.controller;

import com.nxtara.devoluciones.model.CargaMasiva;
import com.nxtara.devoluciones.service.CargaMasivaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/cargas")
public class CargaMasivaController {

    private final CargaMasivaService cargaMasivaService;

    public CargaMasivaController(CargaMasivaService cargaMasivaService) {
        this.cargaMasivaService = cargaMasivaService;
    }

    @PostMapping
    public ResponseEntity<CargaMasiva> cargarArchivo(@RequestParam("archivo") MultipartFile archivo) {
        try {
            CargaMasiva resultado = cargaMasivaService.procesarArchivoCsv(archivo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}