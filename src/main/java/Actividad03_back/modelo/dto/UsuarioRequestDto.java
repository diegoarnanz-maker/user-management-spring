package Actividad03_back.modelo.dto;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String image;
    private List<String> roles;
}
