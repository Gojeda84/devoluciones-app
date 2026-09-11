package com.nxtara.devoluciones.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cargas_masivas")
public class CargaMasiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Column(name = "total_filas", nullable = false)
    private Integer totalFilas;

    @Column(name = "filas_exitosas", nullable = false)
    private Integer filasExitosas;

    @Column(name = "filas_fallidas", nullable = false)
    private Integer filasFallidas;

    @Column(name = "fecha_carga")
    private LocalDateTime fechaCarga = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carga_id")
    private List<CargaError> errores = new ArrayList<>();

    // Constructores, Getters y Setters
    public CargaMasiva() {}

    public Long getId() { return id; }
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    public Integer getTotalFilas() { return totalFilas; }
    public void setTotalFilas(Integer totalFilas) { this.totalFilas = totalFilas; }
    public Integer getFilasExitosas() { return filasExitosas; }
    public void setFilasExitosas(Integer filasExitosas) { this.filasExitosas = filasExitosas; }
    public Integer getFilasFallidas() { return filasFallidas; }
    public void setFilasFallidas(Integer filasFallidas) { this.filasFallidas = filasFallidas; }
    public List<CargaError> getErrores() { return errores; }
    public void setErrores(List<CargaError> errores) { this.errores = errores; }
}