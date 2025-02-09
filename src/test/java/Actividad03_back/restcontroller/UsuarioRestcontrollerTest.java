package Actividad03_back.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import Actividad03_back.modelo.entities.Rol;
import Actividad03_back.modelo.entities.Usuario;
import Actividad03_back.modelo.services.IUsuarioService;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsuarioRestcontrollerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private IUsuarioService usuarioService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private Usuario testUser;

        @BeforeEach
        public void setUp() {
                testUser = Usuario.builder()
                                .firstName("Test")
                                .lastName("User")
                                .username("testuser")
                                .email("testuser@example.com")
                                .password(passwordEncoder.encode("12345678"))
                                .roles(List.of(new Rol(2L, "ROLE_USER")))
                                .build();

                usuarioService.create(testUser);
        }

        @Test
        @WithMockUser(username = "testuser", roles = { "USER" })
        public void testListarUsuariosPaginacion() throws Exception {
                mockMvc.perform(get("/api/user?page=0&perPage=10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.results").isArray());
        }

        @Test
        @WithMockUser(username = "testuser", roles = { "USER" })
        public void testBuscarUsuarioPorId() throws Exception {
                // Caso éxito
                Long userId = testUser.getIdUsuario();
                mockMvc.perform(get("/api/user/" + userId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.username").value("testuser"))
                                .andExpect(jsonPath("$.email").value("testuser@example.com"));

                // Caso error
                mockMvc.perform(get("/api/user/1000"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("No se ha encontrado el usuario con id 1000"));

                // Caso error
                mockMvc.perform(get("/api/user/"))
                                .andExpect(status().isNotFound());
        }
}
