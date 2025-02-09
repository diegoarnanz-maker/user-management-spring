package Actividad03_back.restcontroller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Actividad03_back.modelo.dto.UsuarioDto;
import Actividad03_back.modelo.services.IUsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UsuarioRestcontroller {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int perPage) {
        try {
            UsuarioDto response = usuarioService.findAllPaginated(page, perPage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error interno al recuperar los usuarios"));
        }
    }

    @GetMapping("/{idUsuario}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long idUsuario) {
        Optional<?> usuario = usuarioService.read(idUsuario);
        if (usuario.isPresent()) {
            return ResponseEntity.status(200).body(usuario);
        } else {
            return ResponseEntity.status(404)
                    .body(Collections.singletonMap("error", "No se ha encontrado el usuario con id " + idUsuario));
        }
    }
}