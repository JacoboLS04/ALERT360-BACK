package co.edu.uniquindio.alert360_BACK.security.dto;

import co.edu.uniquindio.alert360_BACK.security.entity.Comment;
import co.edu.uniquindio.alert360_BACK.security.enums.StatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReportDto {

    private int id;

    @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 300 caracteres")
    @NotEmpty(message = "El titulo no puede estar vacío")
    private String title;

    @NotNull(message = "Las categorías no puede estar vacía")
    private List<CategoryDto> categories;

    @Size(min = 5, max = 1000, message = "La descripción debe tener entre 5 y 1000 caracteres")
    @NotEmpty(message = "La descripción no puede estar vacía")
    private String description;


    private double latitude;

    private double longitude;

    @NotNull(message = "Debe subir al menos una imagen")
    private List<String> images;

    private boolean isVerified;

    @NotEmpty(message = "El id del usuario no puede estar vacío")
    private Integer userId;

    @NotEmpty(message = "El correo no puede estar vacío")
    private String email;

    private List<CommentDto> comments;
    private String createdByEmail;

    @NotEmpty(message = "La ubicación no puede estar vacía")
    private String location;

    private int importanceCount;
    private StatusEnum status;
    private String rejectionReason;
    private LocalDateTime modificationDeadline;



}
