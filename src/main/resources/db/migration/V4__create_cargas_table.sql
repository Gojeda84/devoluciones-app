CREATE TABLE cargas_masivas (
    id BIGSERIAL PRIMARY KEY,
    nombre_archivo VARCHAR(255) NOT NULL,
    total_filas INT NOT NULL,
    filas_exitosas INT NOT NULL,
    filas_fallidas INT NOT NULL,
    fecha_carga TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE carga_errores (
    id BIGSERIAL PRIMARY KEY,
    carga_id BIGINT REFERENCES cargas_masivas(id) ON DELETE CASCADE,
    num_fila INT NOT NULL,
    campo VARCHAR(100),
    motivo VARCHAR(255) NOT NULL
);