package com.nxtara.devoluciones.repository;

import com.nxtara.devoluciones.domain.Solicitud;
import com.nxtara.devoluciones.domain.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    Optional<Solicitud> findByFolio(String folio);
    List<Solicitud> findByEstado(EstadoSolicitud estado);
    List<Solicitud> findByRutCliente(String rutCliente);
    boolean existsByReferenciaBanco(String referenciaBanco);
}