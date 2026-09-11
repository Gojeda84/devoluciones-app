package com.nxtara.devoluciones.repository;

import com.nxtara.devoluciones.model.CargaMasiva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargaMasivaRepository extends JpaRepository<CargaMasiva, Long> {
}