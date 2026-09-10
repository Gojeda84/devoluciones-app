CREATE TABLE solicitud (
    id BIGSERIAL PRIMARY KEY,
    folio VARCHAR(20) NOT NULL UNIQUE,
    rut_cliente VARCHAR(12) NOT NULL,
    nombre_cliente VARCHAR(100) NOT NULL,
    monto NUMERIC(12, 2) NOT NULL CHECK (monto > 0 AND monto <= 10000000),
    moneda VARCHAR(3) NOT NULL DEFAULT 'CLP',
    banco_destino VARCHAR(50) NOT NULL,
    cuenta_destino VARCHAR(30) NOT NULL,
    referencia_banco VARCHAR(50) UNIQUE,
    origen VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    motivo_rechazo TEXT,
    reabierta_count INT NOT NULL DEFAULT 0,
    creada_por VARCHAR(50) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizada_por VARCHAR(50) NOT NULL,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE evento_solicitud (
    id BIGSERIAL PRIMARY KEY,
    solicitud_id BIGINT NOT NULL REFERENCES solicitud(id),
    estado_origen VARCHAR(20),
    estado_destino VARCHAR(20) NOT NULL,
    usuario VARCHAR(50) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comentario TEXT
);

CREATE INDEX idx_solicitud_estado ON solicitud(estado);
CREATE INDEX idx_solicitud_rut ON solicitud(rut_cliente);
