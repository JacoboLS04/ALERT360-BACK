package co.edu.uniquindio.alert360_BACK.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {

    private String id;

    @Size(min = 2, max = 20,message = "El nombre de la categoría debe tener entre 2 y 20 caracteres")
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;

    @Size(min = 2, message = "La descripción de la categoría debe tener mas de 2 caracteres")
    @NotBlank(message = "La descripción de la categoría es obligatoria")
    private String description;



}
