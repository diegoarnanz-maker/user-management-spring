package Actividad03_back.modelo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Actividad03_back.modelo.entities.Rol;
import Actividad03_back.repository.IRolRepository;

@Repository
public class RolServiceImplMy8 implements IRolService {

    @Autowired
    private IRolRepository rolRepository;

    @Override
    public Optional<Rol> findByNombre(String nombre) {
        try{
            if(nombre == null || nombre.isEmpty()){
                throw new IllegalArgumentException("El nombre del rol no puede ser nulo o vacío");
            }
            return rolRepository.findByNombre(nombre);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al intentar recuperar el rol por nombre");
        }
    }

    @Override
    public Optional<Rol> create(Rol rol) {
        try{
            if(rol == null){
                throw new IllegalArgumentException("El rol no puede ser nulo");
            }
            return Optional.of(rolRepository.save(rol));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al intentar crear el rol");
        }
    }

}
