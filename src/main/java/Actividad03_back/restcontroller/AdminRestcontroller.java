package Actividad03_back.restcontroller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Actividad03_back.modelo.dto.UsuarioRequestDto;
import Actividad03_back.modelo.entities.Rol;
import Actividad03_back.modelo.entities.Usuario;
import Actividad03_back.modelo.services.IRolService;
import Actividad03_back.modelo.services.IUsuarioService;

@RestController
@RequestMapping("/api/admin")
public class AdminRestcontroller {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRolService rolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/newuser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioRequestDto usuarioDto) {
        try {
            List<Rol> roles = usuarioDto.getRoles().stream()
                    .map(rolService::findByNombre)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            if (roles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Los roles especificados no existen"));
            }

            String passwordEncriptada = passwordEncoder.encode(usuarioDto.getPassword());

            Usuario usuario = Usuario.builder()
                    .firstName(usuarioDto.getFirstName())
                    .lastName(usuarioDto.getLastName())
                    .username(usuarioDto.getUsername())
                    .email(usuarioDto.getEmail())
                    .password(passwordEncriptada)
                    .image(usuarioDto.getImage())
                    .roles(roles)
                    .build();

            Optional<Usuario> usuarioCreado = usuarioService.create(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error interno al guardar el usuario"));
        }
    }

    @PutMapping("/updateuser/{idUsuario}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long idUsuario,
            @RequestBody UsuarioRequestDto usuarioDto) {
        try {
            Optional<Usuario> usuarioExistente = usuarioService.read(idUsuario);
            if (usuarioExistente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "No se encontró el usuario con id " + idUsuario));
            }

            List<Rol> roles = usuarioDto.getRoles().stream()
                    .map(rolService::findByNombre)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            if (roles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "Los roles especificados no existen"));
            }

            Usuario usuarioActualizado = usuarioExistente.get();
            usuarioActualizado.setFirstName(usuarioDto.getFirstName());
            usuarioActualizado.setLastName(usuarioDto.getLastName());
            usuarioActualizado.setUsername(usuarioDto.getUsername());
            usuarioActualizado.setEmail(usuarioDto.getEmail());

            if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isEmpty()) {
                usuarioActualizado.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
            }

            usuarioActualizado.setImage(usuarioDto.getImage());
            usuarioActualizado.setRoles(roles);

            Usuario usuarioGuardado = usuarioService.update(usuarioActualizado);

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
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
