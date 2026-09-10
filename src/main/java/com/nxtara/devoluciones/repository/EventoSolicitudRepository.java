package com.nxtara.devoluciones.repository;

import com.nxtara.devoluciones.domain.EventoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoSolicitudRepository extends JpaRepository<EventoSolicitud, Long> {
    List<EventoSolicitud> findBySolicitudIdOrderByFechaDesc(Long solicitudId);
}