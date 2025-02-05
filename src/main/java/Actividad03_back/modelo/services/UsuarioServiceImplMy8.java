package Actividad03_back.modelo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import Actividad03_back.modelo.dto.UsuarioDto;
import Actividad03_back.modelo.entities.Usuario;
import Actividad03_back.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImplMy8 implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        try {
            return usuarioRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al intentar recuperar los usuarios de la base de datos");
        }
    }

    @Override
    public UsuarioDto findAllPaginated(int page, int perPage) {
        try {
            Page<Usuario> usuariosPage = usuarioRepository.findAll(PageRequest.of(page, perPage));

            return UsuarioDto.builder()
                    .page(page)
                    .per_page(perPage)
                    .total((int) usuariosPage.getTotalElements())
                    .total_pages(usuariosPage.getTotalPages())
                    .results(usuariosPage.getContent())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al intentar recuperar los usuarios con paginación");
        }
    }

    @Override
    public Optional<Usuario> create(Usuario entity) {
        if (entity == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (entity.getIdUsuario() != null && usuarioRepository.existsById(entity.getIdUsuario())) {
            throw new IllegalArgumentException("El id del usuario: " + entity.getIdUsuario() + "ya existe");
        }
        return Optional.of(usuarioRepository.save(entity));
    }

    @Override
    public Optional<Usuario> read(Long id) {
        try{
            if(id == null){
                throw new IllegalArgumentException("El id del usuario no puede ser nulo");
            }
            return usuarioRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al intentar recuperar el usuario de la base de datos");
        }
    }

    @Override
    public Usuario update(Usuario entity) {
        try{
            if(entity == null){
                throw new IllegalArgumentException("El usuario no puede ser nulo");
            }
            if(entity.getIdUsuario() == null || !usuarioRepository.existsById(entity.getIdUsuario())){
                throw new IllegalArgumentException("El id del usuario no puede ser nulo o no existe");
            }
            return usuarioRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al intentar actualizar el usuario en la base de datos");
        }
    }

    @Override
    public void delete(Long id) {
        try{
            if(id == null){
                throw new IllegalArgumentException("El id del usuario no puede ser nulo");
            }
            if(!usuarioRepository.existsById(id)){
                throw new IllegalArgumentException("El id del usuario no existe");
            }
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al intentar eliminar el usuario de la base de datos");
        }
    }

}
