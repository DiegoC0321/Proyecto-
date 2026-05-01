-- ============================================================
--  GESTOR DE PROYECTOS PERSONALES Y ENTREGAS UNIVERSITARIAS
--  Script MySQL - Compatible con phpMyAdmin / XAMPP
-- ============================================================

CREATE DATABASE IF NOT EXISTS gestor_proyectos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE gestor_proyectos;

-- ------------------------------------------------------------
-- 1. USUARIO
-- ------------------------------------------------------------
CREATE TABLE usuario (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100)  NOT NULL,
    correo        VARCHAR(150)  NOT NULL UNIQUE,
    contrasena    VARCHAR(255)  NOT NULL,          -- bcrypt hash
    fecha_creacion TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- 2. ESTADO (columnas del tablero Kanban)
-- ------------------------------------------------------------
CREATE TABLE estado (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado  VARCHAR(50)  NOT NULL,
    orden_columna  INT          NOT NULL DEFAULT 0
);

-- ------------------------------------------------------------
-- 3. PROYECTO
-- ------------------------------------------------------------
CREATE TABLE proyecto (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(150) NOT NULL,
    descripcion    TEXT,
    fecha_creacion TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    usuario_id     INT          NOT NULL,
    CONSTRAINT fk_proyecto_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- 4. TAREA
-- ------------------------------------------------------------
CREATE TABLE tarea (
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    titulo               VARCHAR(200) NOT NULL,
    descripcion          TEXT,
    fecha_limite         DATE,
    proyecto_id          INT          NOT NULL,
    usuario_asignado_id  INT,
    estado_id            INT          NOT NULL,
    CONSTRAINT fk_tarea_proyecto
        FOREIGN KEY (proyecto_id)         REFERENCES proyecto(id) ON DELETE CASCADE,
    CONSTRAINT fk_tarea_usuario
        FOREIGN KEY (usuario_asignado_id) REFERENCES usuario(id)  ON DELETE SET NULL,
    CONSTRAINT fk_tarea_estado
        FOREIGN KEY (estado_id)           REFERENCES estado(id)   ON DELETE RESTRICT
);

-- ------------------------------------------------------------
-- 5. ETIQUETA
-- ------------------------------------------------------------
CREATE TABLE etiqueta (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nombre         VARCHAR(80)  NOT NULL,
    color_hex      VARCHAR(7)   NOT NULL DEFAULT '#6c757d'  -- ej: #FF5733
);

-- ------------------------------------------------------------
-- 6. TAREA_ETIQUETA  (relación N:M)
-- ------------------------------------------------------------
CREATE TABLE tarea_etiqueta (
    tarea_id    INT NOT NULL,
    etiqueta_id INT NOT NULL,
    PRIMARY KEY (tarea_id, etiqueta_id),
    CONSTRAINT fk_te_tarea
        FOREIGN KEY (tarea_id)    REFERENCES tarea(id)    ON DELETE CASCADE,
    CONSTRAINT fk_te_etiqueta
        FOREIGN KEY (etiqueta_id) REFERENCES etiqueta(id) ON DELETE CASCADE
);


-- ============================================================
--  DATOS DE PRUEBA
-- ============================================================

-- Estados por defecto del Kanban
INSERT INTO estado (nombre_estado, orden_columna) VALUES
    ('Pendiente',    1),
    ('En Progreso',  2),
    ('En Revisión',  3),
    ('Finalizado',   4);

-- Usuarios de prueba (contraseñas hasheadas con bcrypt en producción)
INSERT INTO usuario (nombre, correo, contrasena) VALUES
    ('Andrea Fonseca',  'andrea@email.com', '$2a$10$hash_ejemplo_andrea'),
    ('Diego Cobos',     'diego@email.com',  '$2a$10$hash_ejemplo_diego'),
    ('Estudiante Demo', 'demo@email.com',   '$2a$10$hash_ejemplo_demo');

-- Proyectos de prueba
INSERT INTO proyecto (nombre, descripcion, usuario_id) VALUES
    ('Proyecto Bases de Datos',  'Trabajo final semestre - BD relacional', 1),
    ('Tesis de Grado',           'Investigación sobre sistemas distribuidos', 2),
    ('Parcial de Algoritmos',    'Preparación parcial 2 - ordenamiento', 1);

-- Tareas de prueba
INSERT INTO tarea (titulo, descripcion, fecha_limite, proyecto_id, usuario_asignado_id, estado_id) VALUES
    ('Diseñar el DER',           'Diagrama entidad-relación del sistema',        '2025-05-20', 1, 1, 1),
    ('Crear script SQL',         'Script de creación e inserción de datos',      '2025-05-22', 1, 2, 2),
    ('Redactar marco teórico',   'Capítulo 1 y 2 de la tesis',                   '2025-06-01', 2, 2, 2),
    ('Implementar Quicksort',    'Código en Java con análisis de complejidad',   '2025-05-18', 3, 1, 3),
    ('Revisar Spring Security',  'Integrar autenticación JWT al backend',        '2025-05-25', 1, 1, 1);

-- Etiquetas de prueba
INSERT INTO etiqueta (nombre, color_hex) VALUES
    ('Urgente',      '#DC3545'),
    ('Backend',      '#0D6EFD'),
    ('Frontend',     '#198754'),
    ('Base de Datos','#FD7E14'),
    ('Documentación','#6C757D');

-- Relaciones tarea ↔ etiqueta
INSERT INTO tarea_etiqueta (tarea_id, etiqueta_id) VALUES
    (1, 4),   -- Diseñar DER → Base de Datos
    (1, 5),   -- Diseñar DER → Documentación
    (2, 4),   -- Script SQL → Base de Datos
    (2, 2),   -- Script SQL → Backend
    (3, 5),   -- Marco teórico → Documentación
    (4, 2),   -- Quicksort → Backend
    (4, 1),   -- Quicksort → Urgente
    (5, 2),   -- Spring Security → Backend
    (5, 1);   -- Spring Security → Urgente
