package com.nxtara.devoluciones.controller;

import com.nxtara.devoluciones.domain.EstadoSolicitud;
import com.nxtara.devoluciones.domain.Solicitud;
import com.nxtara.devoluciones.service.SolicitudService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestBody CambiarEstadoRequest request,
            @RequestHeader(value = "X-User-Name", defaultValue = "sistema") String usuario,
            @RequestHeader(value = "X-User-Role", defaultValue = "ANALISTA") String rol) {
        
        Solicitud actualizada = solicitudService.cambiarEstado(
                id, 
                request.getNuevoEstado(), 
                usuario, 
                rol, 
                request.getComentario()
        );
        return ResponseEntity.ok(actualizada);
    }

    @Data
    public static class CambiarEstadoRequest {
        private EstadoSolicitud nuevoEstado;
        private String comentario;
    }
}