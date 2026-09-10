CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL
);

INSERT INTO usuario (username, password, rol) VALUES 
('analista1', '$2a$10$e8R7/sD/V9oW/bNlhR0o6e3R1.k9f3L4vUe.S1gR71y4I5bX5x2a6', 'ANALISTA'),
('supervisor1', '$2a$10$e8R7/sD/V9oW/bNlhR0o6e3R1.k9f3L4vUe.S1gR71y4I5bX5x2a6', 'SUPERVISOR');

INSERT INTO solicitud (folio, rut_cliente, nombre_cliente, monto, moneda, banco_destino, cuenta_destino, origen, estado, creada_por, actualizada_por)
VALUES ('DEV-2026-000001', '12345678-5', 'MARIA PEREZ SOTO', 150000.00, 'CLP', 'BANCO CHILE', '001234567890', 'MANUAL', 'BORRADOR', 'analista1', 'analista1');

INSERT INTO evento_solicitud (solicitud_id, estado_origen, estado_destino, usuario, comentario)
VALUES (1, NULL, 'BORRADOR', 'analista1', 'Creación inicial');
