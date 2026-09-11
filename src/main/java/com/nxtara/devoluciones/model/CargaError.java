package com.nxtara.devoluciones.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carga_errores")
public class CargaError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_fila", nullable = false)
    private Integer numFila;

    @Column(name = "campo")
    private String campo;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    public CargaError() {}

    public CargaError(Integer numFila, String campo, String motivo) {
        this.numFila = numFila;
        this.campo = campo;
        this.motivo = motivo;
    }

    public Long getId() { return id; }
    public Integer getNumFila() { return numFila; }
    public String getCampo() { return campo; }
    public String getMotivo() { return motivo; }
}