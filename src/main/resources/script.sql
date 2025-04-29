-- Script de creación de base de datos para H2
-- Autor: Daniel Segovia
-- Evaluación Java

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS usuario (
                                       id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       nombre VARCHAR(255) NOT NULL,
    correo VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    creado TIMESTAMP NOT NULL,
    modificado TIMESTAMP NOT NULL,
    ultimo_login TIMESTAMP,
    token VARCHAR(500),
    CONSTRAINT uk_correo UNIQUE (correo)
    );

-- Crear tabla de teléfonos
CREATE TABLE IF NOT EXISTS telefono (
                                        id_telefono BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        numero VARCHAR(20) NOT NULL,
    codigo_ciudad VARCHAR(10) NOT NULL,
    codigo_pais VARCHAR(10) NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario_telefono
    FOREIGN KEY (usuario_id)
    REFERENCES usuario(id_usuario)
    ON DELETE CASCADE
    );

-- Índices para mejorar performance
CREATE INDEX idx_usuario_correo ON usuario(correo);
CREATE INDEX idx_telefono_usuario ON telefono(usuario_id);


INSERT INTO usuario (nombre, correo, contrasena, activo, creado, modificado, ultimo_login)
VALUES ('Usuario Admin', 'admin@example.com', 'Dsegovia94@', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
