package Actividad03_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Actividad03_back.modelo.entities.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

    Usuario findByEmail(String email);
}
