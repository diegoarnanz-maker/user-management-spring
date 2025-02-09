package Actividad03_back.restcontroller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Actividad03_back.modelo.entities.Usuario;
import Actividad03_back.modelo.services.IUsuarioService;

@RestController
@RequestMapping("/api/admin")
public class AdminRestcontroller {

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/newuser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Optional<Usuario> usuarioCreado = usuarioService.create(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error interno al guardar el usuario"));
        }
    }

    @PutMapping("/updateuser/{idUsuario}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long idUsuario, @RequestBody Usuario usuario) {
        try {
            usuario.setIdUsuario(idUsuario);
            Usuario usuarioActualizado = usuarioService.update(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioActualizado);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error interno al actualizar el usuario"));
        }
    }

    @DeleteMapping("/deleteuser/{idUsuario}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long idUsuario) {
        try {
            Optional<Usuario> usuario = usuarioService.read(idUsuario);
            if (usuario.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Collections.singletonMap("error", "No se ha encontrado el usuario con id " + idUsuario));
            }
            usuarioService.delete(idUsuario);
            return ResponseEntity.status(200)
                    .body(Collections.singletonMap("success", "Usuario eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
