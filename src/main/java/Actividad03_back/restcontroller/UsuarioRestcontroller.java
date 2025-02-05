package Actividad03_back.restcontroller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Actividad03_back.modelo.dto.UsuarioDto;
import Actividad03_back.modelo.entities.Usuario;
import Actividad03_back.modelo.services.IUsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UsuarioRestcontroller {

    @Autowired
    private IUsuarioService usuarioService;

    // FindAll() incluida paginacion
    @GetMapping
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

    // Read()
    @GetMapping("/user/{id}")
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Long idUsuario) {
        Optional<?> usuario = usuarioService.read(idUsuario);
        if (usuario.isPresent()) {
            return ResponseEntity.status(200).body(usuario);
        } else {
            return ResponseEntity.status(404)
                    .body(Collections.singletonMap("error", "No se ha encontrado el usuario con id " + idUsuario));
        }
    }

    // Create()
    @PostMapping("/newuser")
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

    // Update()
    @PutMapping("/updateuser/{id}")
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

    // Delete()
    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long idUsuario) {
        try {
            // Esta Presente?
            Optional<Usuario> usuario = usuarioService.read(idUsuario);
            if (usuario.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Collections.singletonMap("error", "No se ha encontrado el usuario con id " + idUsuario));
            }
            return ResponseEntity.status(200)
                    .body(Collections.singletonMap("success", "Usuario eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
