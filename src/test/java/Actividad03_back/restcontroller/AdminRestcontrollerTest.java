package Actividad03_back.restcontroller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import Actividad03_back.modelo.entities.Usuario;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminRestcontrollerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCrearUsuario() throws Exception {
        Usuario newUsuario = Usuario.builder()
                .firstName("Nuevo")
                .lastName("Usuario")
                .username("nuevousuario2")
                .email("2nuevousuario@gmail.com")
                .image("https://example.com/image.jpg")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String usuarioJson = objectMapper.writeValueAsString(newUsuario);

        mockMvc.perform(post("/api/newuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").exists())
                .andExpect(jsonPath("$.username").value("nuevousuario2"))
                .andExpect(jsonPath("$.email").value("2nuevousuario@gmail.com"));
    }

    @Test
    public void testActualizarUsuario() throws Exception {
        Usuario usuarioActualizado = Usuario.builder()
                .firstName("NuevoNombre")
                .lastName("NuevoApellido")
                .username("usuarioactualizado")
                .email("usuario.actualizado@example.com")
                .image("https://example.com/image-updated.jpg")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String usuarioJson = objectMapper.writeValueAsString(usuarioActualizado);

        Long idUsuario = 1L;

        mockMvc.perform(put("/api/updateuser/" + idUsuario)
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(idUsuario))
                .andExpect(jsonPath("$.firstName").value("NuevoNombre"))
                .andExpect(jsonPath("$.lastName").value("NuevoApellido"))
                .andExpect(jsonPath("$.username").value("usuarioactualizado"))
                .andExpect(jsonPath("$.email").value("usuario.actualizado@example.com"));
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/api/deleteuser/" + userId))
                .andExpect(status().isOk()) // Espera 200 OK
                .andExpect(jsonPath("$.success").value("Usuario eliminado correctamente"));

    }
}
