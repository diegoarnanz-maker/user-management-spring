package Actividad03_back.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import Actividad03_back.modelo.dto.UsuarioRequestDto;
import Actividad03_back.modelo.entities.Rol;
import Actividad03_back.modelo.entities.Usuario;
import Actividad03_back.modelo.services.IRolService;
import Actividad03_back.modelo.services.IUsuarioService;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminRestcontrollerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private IUsuarioService usuarioService;

        @Autowired
        private IRolService rolService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void testCrearUsuarioSinDto() throws Exception {
                UsuarioRequestDto newUsuarioDto = UsuarioRequestDto.builder()
                                .firstName("Nuevo2")
                                .lastName("Usuario2")
                                .username("nuevousuariodtorole2")
                                .email("nuevorole2.usuario@example.com")
                                .password("12345678")
                                .image("https://ui-avatars.com/api/?name=Nuevo+Usuario&size=250")
                                .roles(List.of("ROLE_USER"))
                                .build();

                ObjectMapper objectMapper = new ObjectMapper();
                String usuarioJson = objectMapper.writeValueAsString(newUsuarioDto);

                System.out.println("JSON enviado: " + usuarioJson);

                MvcResult result = mockMvc.perform(post("/api/admin/newuser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(usuarioJson))
                                .andReturn();

                System.out.println("Response Status: " + result.getResponse().getStatus());
                System.out.println("Response Body: " + result.getResponse().getContentAsString());

                mockMvc.perform(post("/api/admin/newuser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(usuarioJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.idUsuario").exists())
                                .andExpect(jsonPath("$.username").value("nuevousuariodtorole2"))
                                .andExpect(jsonPath("$.roles[0].nombre").value("ROLE_USER"));
        }

}
