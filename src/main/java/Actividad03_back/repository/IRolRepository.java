package Actividad03_back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Actividad03_back.modelo.entities.Rol;

public interface IRolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}