-- Script de creación de base de datos para H2
-- Autor: Daniel Segovia
-- Evaluación Java

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS usuario (
                                       id_usuario  UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    nombre      VARCHAR(255) NOT NULL,
    correo      VARCHAR(255) NOT NULL UNIQUE,
    contrasena  VARCHAR(255) NOT NULL,
    activo      BOOLEAN      DEFAULT TRUE,
    creado      TIMESTAMP    NOT NULL,
    modificado  TIMESTAMP,
    ultimo_login TIMESTAMP,
    token       VARCHAR(500)
    );

CREATE TABLE IF NOT EXISTS telefono (
                                        id_telefono UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    numero        VARCHAR(20) NOT NULL,
    codigo_ciudad VARCHAR(10) NOT NULL,
    codigo_pais   VARCHAR(10) NOT NULL,
    usuario_id    UUID        NOT NULL,
    CONSTRAINT fk_usuario_telefono
    FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario)
    ON DELETE CASCADE
    );

CREATE INDEX idx_usuario_correo  ON usuario(correo);
CREATE INDEX idx_telefono_usuario ON telefono(usuario_id);


INSERT INTO usuario (nombre, correo, contrasena, activo, creado, modificado, ultimo_login)
VALUES ('Usuario Admin', 'admin@example.com', 'Dsegovia94@',
        TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
