AppAngular: https://github.com/diegoarnanz-maker/user-management-angular
AppSpring: https://github.com/diegoarnanz-maker/user-management-spring

Script:
CREATE DATABASE IF NOT EXISTS actividad03_mod;
USE actividad03_mod;

CREATE TABLE IF NOT EXISTS roles (
    id_rol BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    image VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS usuario_roles (
    id_usuario BIGINT NOT NULL,
    id_rol BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol) ON DELETE CASCADE
);

INSERT INTO roles (nombre) VALUES
('ROLE_ADMIN'),
('ROLE_USER');

INSERT INTO usuarios (first_name, last_name, username, email, password, image) VALUES
-- Admin
('Admin', 'Master', 'admin', 'admin@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Admin&size=250'),

('Juan', 'Pérez', 'juanperez', 'juan.perez@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Juan+Perez&size=250'),
('María', 'Gómez', 'mariagomez', 'maria.gomez@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Maria+Gomez&size=250'),
('Carlos', 'Ramírez', 'carlosramirez', 'carlos.ramirez@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Carlos+Ramirez&size=250'),
('Ana', 'Fernández', 'anafernandez', 'ana.fernandez@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Ana+Fernandez&size=250'),
('Pedro', 'Sánchez', 'pedrosanchez', 'pedro.sanchez@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Pedro+Sanchez&size=250'),
('Lucía', 'Hernández', 'luciahernandez', 'lucia.hernandez@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Lucia+Hernandez&size=250'),
('Diego', 'López', 'diegolopez', 'diego.lopez@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Diego+Lopez&size=250'),
('Paula', 'Martínez', 'paulamartinez', 'paula.martinez@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Paula+Martinez&size=250'),
('Fernando', 'García', 'fernandogarcia', 'fernando.garcia@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Fernando+Garcia&size=250'),
('Elena', 'Ruiz', 'elenaruiz', 'elena.ruiz@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Elena+Ruiz&size=250'),
('Javier', 'Torres', 'javiertorres', 'javier.torres@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Javier+Torres&size=250'),
('Sara', 'Navarro', 'saranavarro', 'sara.navarro@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Sara+Navarro&size=250'),
('Hugo', 'Castro', 'hugocastro', 'hugo.castro@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Hugo+Castro&size=250'),
('Andrea', 'Morales', 'andreamorales', 'andrea.morales@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Andrea+Morales&size=250'),
('David', 'Ortega', 'davidortega', 'david.ortega@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=David+Ortega&size=250'),
('Clara', 'Santos', 'clarasantos', 'clara.santos@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Clara+Santos&size=250'),
('Alberto', 'Méndez', 'albertomendez', 'alberto.mendez@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Alberto+Mendez&size=250'),
('Cristina', 'Rojas', 'cristinarojas', 'cristina.rojas@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Cristina+Rojas&size=250'),
('Manuel', 'Vega', 'manuelvega', 'manuel.vega@example.com', '$2a$10$jPCn3SqKcjddmKovqdlIxeMoyOmOsEzFtUSXcqHz.mEhRsJCW9ACW', 'https://ui-avatars.com/api/?name=Manuel+Vega&size=250');

INSERT INTO usuario_roles (id_usuario, id_rol) VALUES
(1, 1);

INSERT INTO usuario_roles (id_usuario, id_rol)
SELECT id_usuario, 2 FROM usuarios WHERE id_usuario > 1;






