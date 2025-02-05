package Actividad03_back.modelo.services;

import Actividad03_back.modelo.dto.UsuarioDto;
import Actividad03_back.modelo.entities.Usuario;

public interface IUsuarioService extends IGenericoCRUD<Usuario, Long> {
    UsuarioDto findAllPaginated(int page, int perPage);
}
