package Actividad03_back.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class UsuarioRestcontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListarUsuariosPaginacion() throws Exception {

        // Caso exito
        mockMvc.perform(get("/api?page=0&perPage=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results.length()").value(10))
                .andExpect(jsonPath("$.results[0].idUsuario").value(1))
                .andExpect(jsonPath("$.results[0].username").value("juanperez"));

        // Caso error
        mockMvc.perform(get("/api?page=-1&perPage=-5"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testBuscarUsuarioPorId() throws Exception {

        // Caso exito
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("juanperez"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));

        // Caso error
        mockMvc.perform(get("/api/user/1000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No se ha encontrado el usuario con id 1000"));
    }

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
