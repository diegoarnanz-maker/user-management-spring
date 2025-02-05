package Actividad03_back.modelo.dto;

import java.util.List;

import Actividad03_back.modelo.entities.Usuario;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {

    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private List<Usuario> results;
    
}
