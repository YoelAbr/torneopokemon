-- =============================================================================
-- TORNEO CHAMPIONS LEAGUE POKÉMON
-- CARACTERÍSTICAS: 3FN / CON TABLA LOGS INDEPENDIENTE / ENFOQUE GESTIÓN DE RONDAS
-- =============================================================================

CREATE DATABASE IF NOT EXISTS tornepkm;
USE tornepkm;

DROP TABLE IF EXISTS LOGS_ACCESO;
DROP TABLE IF EXISTS RESULTADOS_CHAMPIONS;
DROP TABLE IF EXISTS PARTIDOS;
DROP TABLE IF EXISTS INTEGRANTES_EQUIPO;
DROP TABLE IF EXISTS EQUIPOS_CHAMPIONS;
DROP TABLE IF EXISTS POKEDEX_COMPETITIVA;
DROP TABLE IF EXISTS JUGADORES;
DROP TABLE IF EXISTS USUARIOS;

-- -----------------------------------------------------------------------------
-- 1. BASE DE SEGURIDAD Y AUTENTICACIÓN (LOGIN)
-- -----------------------------------------------------------------------------
CREATE TABLE USUARIOS (
    id_usuario INT AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(15) DEFAULT 'JUGADOR',
    PRIMARY KEY (id_usuario),
    UNIQUE (username),
    CONSTRAINT chk_rol_sistema CHECK (rol IN ('ADMIN', 'JUGADOR'))
);

-- -----------------------------------------------------------------------------
-- 2. TABLA HISTÓRICA DE LOGS DE ACCESO (AJUSTE 3FN)
-- Centraliza los registros de entrada de admins y jugadores sin alterar 'USUARIOS'.
-- -----------------------------------------------------------------------------
CREATE TABLE LOGS_ACCESO (
    id_log INT AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    fecha_hora_login DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_log),
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE
);

-- -----------------------------------------------------------------------------
-- 3. PARTICIPANTES DE LA CHAMPIONS LEAGUE
-- -----------------------------------------------------------------------------
CREATE TABLE JUGADORES (
    id_usuario INT NOT NULL,
    nombre_entrenador VARCHAR(50) NOT NULL,
    puntos_champions INT DEFAULT 0,
    PRIMARY KEY (id_usuario),
    UNIQUE (nombre_entrenador),
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE
);

-- -----------------------------------------------------------------------------
-- 4. ALMACENAMIENTO DE LA POKÉDEX (60 POKEMONS)
-- -----------------------------------------------------------------------------
CREATE TABLE POKEDEX_COMPETITIVA (
    id_pokedex INT AUTO_INCREMENT,
    nombre_pokemon VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_pokedex),
    UNIQUE (nombre_pokemon)
);

-- -----------------------------------------------------------------------------
-- 5. CONTROL DE EQUIPOS DEL COMPETIDOR (6 POKÉMON MÁXIMO)
-- -----------------------------------------------------------------------------
CREATE TABLE EQUIPOS_CHAMPIONS (
    id_equipo INT AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    PRIMARY KEY (id_equipo),
    FOREIGN KEY (id_usuario) REFERENCES USUARIOS(id_usuario) ON DELETE CASCADE
);

CREATE TABLE INTEGRANTES_EQUIPO (
    id_equipo INT NOT NULL,
    id_pokedex INT NOT NULL,
    slot INT NOT NULL, 
    PRIMARY KEY (id_equipo, slot),
    FOREIGN KEY (id_equipo) REFERENCES EQUIPOS_CHAMPIONS(id_equipo) ON DELETE CASCADE,
    FOREIGN KEY (id_pokedex) REFERENCES POKEDEX_COMPETITIVA(id_pokedex),
    CONSTRAINT chk_slot_limite CHECK (slot BETWEEN 1 AND 6),
    UNIQUE (id_equipo, id_pokedex)
);

-- -----------------------------------------------------------------------------
-- 6. CUADRO DE ELIMINATORIAS (BRACKETS)
-- -----------------------------------------------------------------------------
CREATE TABLE PARTIDOS (
    id_partido INT AUTO_INCREMENT,
    id_jugador1 INT NOT NULL,
    id_jugador2 INT NOT NULL,
    ronda VARCHAR(20) NOT NULL, 
    PRIMARY KEY (id_partido),
    FOREIGN KEY (id_jugador1) REFERENCES JUGADORES(id_usuario),
    FOREIGN KEY (id_jugador2) REFERENCES JUGADORES(id_usuario),
    CONSTRAINT chk_rivales_champions CHECK (id_jugador1 <> id_jugador2),
    CONSTRAINT chk_ronda_champions CHECK (ronda IN ('CUARTOS', 'SEMIFINAL', 'FINAL'))
);

-- -----------------------------------------------------------------------------
-- 7. DESENLACES: QUIEN PASA Y QUIEN NO (SIN MARCADOR DE POKÉMON VIVOS)
-- -----------------------------------------------------------------------------
CREATE TABLE RESULTADOS_CHAMPIONS (
    id_resultado INT AUTO_INCREMENT,
    id_partido INT NOT NULL,
    id_ganador INT NOT NULL,   -- Pasa de ronda
    id_perdedor INT NOT NULL,  -- Queda eliminado
    PRIMARY KEY (id_resultado),
    UNIQUE (id_partido), 
    FOREIGN KEY (id_partido) REFERENCES PARTIDOS(id_partido) ON DELETE CASCADE,
    FOREIGN KEY (id_ganador) REFERENCES JUGADORES(id_usuario),
    FOREIGN KEY (id_perdedor) REFERENCES JUGADORES(id_usuario),
    CONSTRAINT chk_logica_resultado CHECK (id_ganador <> id_perdedor)
);

-- =============================================================================
-- INSERCIÓN DE LOS 60 POKÉMON (SOLO NOMBRES)
-- =============================================================================
INSERT INTO POKEDEX_COMPETITIVA (nombre_pokemon) VALUES
('Incineroar'), ('Amoonguss'), ('Rillaboom'), ('Tornadus'), ('Whimsicott'), 
('Grimmsnarl'), ('Pelipper'), ('Clefairy'), ('Sableye'), ('Smeargle'), 
('Flutter Mane'), ('Gholdengo'), ('Kingambit'), ('Urshifu-Rapid'), ('Urshifu-Single'), 
('Chien-Pao'), ('Chi-Yu'), ('Dragonite'), ('Dragapult'), ('Archaludon'), 
('Farigiraf'), ('Porygon2'), ('Glimmora'), ('Ursaluna-Bloodmoon'), ('Ursaluna-Normal'), 
('Teal Mask Ogerpon'), ('Wellspring Ogerpon'), ('Hearthflame Ogerpon'), ('Cornerstone Ogerpon'), ('Great Tusk'), 
('Roaring Moon'), ('Raging Bolt'), ('Gouging Fire'), ('Brute Bonnet'), ('Scream Tail'), 
('Sandy Shocks'), ('Iron Hands'), ('Iron Bundle'), ('Iron Crown'), ('Iron Valiant'), 
('Iron Jugulis'), ('Iron Moth'), ('Iron Boulder'), ('Calyrex-Shadow'), ('Calyrex-Ice'), 
('Miraidon'), ('Koraidon'), ('Terapagos'), ('Zacian-Crowned'), ('Zamazenta-Crowned'), 
('Kyogre'), ('Groudon'), ('Rayquaza'), ('Lunala'), ('Tyranitar'), 
('Metagross'), ('Salamence'), ('Gengar'), ('Sylveon'), ('Charizard');

-- =============================================================================
-- REGISTRO DE CREDENCIALES (1 ADMIN Y 7 JUGADORES)
-- =============================================================================
INSERT INTO USUARIOS (id_usuario, username, password_hash, rol) VALUES 
(1, 'gold_champion', '$2a$12$ExX7z98K.S10a8d7F9gHz.gold_secure_hash', 'JUGADOR'),
(2, 'admin_torneo', '$2a$12$ExX7z98K.S10a8d7F9gHz.admin_secure_hash', 'ADMIN'),
(3, 'ash_k', '$2a$12$ExX7z98K.S10a8d7F9gHz.ash_k_secure_hash', 'JUGADOR'),
(4, 'cynthia_g', '$2a$12$ExX7z98K.S10a8d7F9gHz.cynthia_hash', 'JUGADOR'),
(5, 'steven_s', '$2a$12$ExX7z98K.S10a8d7F9gHz.steven_hash', 'JUGADOR'),
(6, 'lance_d', '$2a$12$ExX7z98K.S10a8d7F9gHz.lance_hash', 'JUGADOR'),
(7, 'leon_g', '$2a$12$ExX7z98K.S10a8d7F9gHz.leon_hash', 'JUGADOR'),
(8, 'blue_o', '$2a$12$ExX7z98K.S10a8d7F9gHz.blue_hash', 'JUGADOR');

-- El Administrador (id_usuario = 2) NO se inserta en JUGADORES porque no compite
INSERT INTO JUGADORES (id_usuario, nombre_entrenador) VALUES 
(1, 'Gold'), (3, 'Ash Ketchum'), (4, 'Cynthia'), (5, 'Steven Stone'), 
(6, 'Lance'), (7, 'Leon'), (8, 'Blue');

-- =============================================================================
-- SIMULACIÓN HISTÓRICA DE LOGS DE ACCESO (EVENTOS DE SOFTWARE)
-- =============================================================================
-- El admin entra a preparar el torneo por la mañana
INSERT INTO LOGS_ACCESO (id_usuario, fecha_hora_login) VALUES (2, '2026-06-04 09:00:00');
-- El jugador Gold accede a revisar el cuadro más tarde
INSERT INTO LOGS_ACCESO (id_usuario, fecha_hora_login) VALUES (1, '2026-06-04 10:15:22');
-- El admin vuelve a entrar a certificar los cierres de ronda
INSERT INTO LOGS_ACCESO (id_usuario, fecha_hora_login) VALUES (2, '2026-06-04 14:30:00');

-- =============================================================================
-- EJEMPLO PRÁCTICO: EL EQUIPO DE GOLD Y SU ELIMINATORIA
-- =============================================================================
INSERT INTO EQUIPOS_CHAMPIONS (id_equipo, id_usuario) VALUES (1, 1);

-- Gold registra sus 6 Pokémon (Incineroar, Flutter Mane, Urshifu, Kingambit, Raging Bolt y Miraidon)
INSERT INTO INTEGRANTES_EQUIPO (id_equipo, id_pokedex, slot) VALUES 
(1, 1, 1), (1, 11, 2), (1, 14, 3), (1, 13, 4), (1, 32, 5), (1, 46, 6);

-- Se programa la eliminatoria: Gold (1) vs Ash (3)
INSERT INTO PARTIDOS (id_partido, id_jugador1, id_jugador2, ronda) VALUES (1, 1, 3, 'CUARTOS');

-- Resultado directo de la app: Gana Gold (Pasa de ronda), Pierde Ash (Eliminado)
INSERT INTO RESULTADOS_CHAMPIONS (id_partido, id_ganador, id_perdedor) VALUES (1, 1, 3);