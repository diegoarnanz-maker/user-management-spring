package Actividad03_back.restcontroller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import Actividad03_back.modelo.dto.LoginDto;
import Actividad03_back.modelo.dto.UsuarioRequestDto;
import Actividad03_back.modelo.entities.Rol;
import Actividad03_back.modelo.entities.Usuario;
import Actividad03_back.modelo.services.IRolService;
import Actividad03_back.modelo.services.IUsuarioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthRestcontroller {

    @Autowired
    private IRolService rolService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        Usuario usuario = usuarioService.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(loginDto.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Contraseña incorrecta"));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                usuario.getUsername(), usuario.getPassword(), usuario.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login exitoso");
        response.put("user", usuario.getUsername());
        response.put("idUsuario", usuario.getIdUsuario());
        response.put("roles", usuario.getRoles().stream().map(r -> r.getNombre()).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Extraemos los datos desde el UserDetails
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();

        // Creo una instancia de Usuario con la información disponible
        Usuario usuario = Usuario.builder()
                .username(userDetails.getUsername())
                .roles(userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> new Rol(null, grantedAuthority.getAuthority()))
                        .toList())
                .build();

        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UsuarioRequestDto usuarioDto) {
        try {
            if (usuarioDto.getUsername() == null || usuarioDto.getUsername().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "El nombre de usuario es obligatorio."));
            }
            if (usuarioDto.getEmail() == null || usuarioDto.getEmail().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "El email es obligatorio."));
            }
            if (usuarioDto.getPassword() == null || usuarioDto.getPassword().length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "La contraseña debe tener al menos 6 caracteres."));
            }

            if (usuarioService.findByUsername(usuarioDto.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "El nombre de usuario ya está en uso."));
            }

            if (usuarioService.findByEmail(usuarioDto.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("error", "El email ya está registrado."));
            }

            Optional<Rol> userRole = rolService.findByNombre("ROLE_USER");

            if (userRole.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("error",
                                "El rol ROLE_USER no está configurado en la base de datos."));
            }

            String passwordEncriptada = passwordEncoder.encode(usuarioDto.getPassword());

            Usuario usuario = Usuario.builder()
                    .firstName(usuarioDto.getFirstName())
                    .lastName(usuarioDto.getLastName())
                    .username(usuarioDto.getUsername())
                    .email(usuarioDto.getEmail())
                    .password(passwordEncriptada)
                    .image(usuarioDto.getImage())
                    .roles(Collections.singletonList(userRole.get()))
                    .build();

            Optional<Usuario> usuarioCreado = usuarioService.create(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error interno al registrar el usuario."));
        }
    }

}
