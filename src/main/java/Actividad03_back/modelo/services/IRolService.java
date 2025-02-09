package Actividad03_back.modelo.services;

import java.util.Optional;

import Actividad03_back.modelo.entities.Rol;

public interface IRolService {
    Optional<Rol> findByNombre(String nombre);
    Optional<Rol> create(Rol rol);
}
