package Actividad03_back.modelo.services;

import java.util.Optional;

import Actividad03_back.modelo.dto.UsuarioDto;
import Actividad03_back.modelo.entities.Usuario;

public interface IUsuarioService extends IGenericoCRUD<Usuario, Long> {
    UsuarioDto findAllPaginated(int page, int perPage);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);
}
