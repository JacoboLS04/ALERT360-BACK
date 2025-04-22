package co.edu.uniquindio.alert360_BACK.security.entity;

import co.edu.uniquindio.alert360_BACK.security.enums.StatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "reports")
public class ReportEntity {
    private int id;
    private String title;
    private List<Category> categories = new ArrayList<>();
    private String description;
    private double latitude;
    private double longitude;
    private List<String> images = new ArrayList<>();
    private LocalDateTime date;
    private boolean isVerified;
    private int userId;
    private String email;
    private List<Comment> comments = new ArrayList<>();
    private String createdByEmail;
    private String location;

    // Nuevos atributos
    private StatusEnum status; // PENDIENTE, VERIFICADO, RECHAZADO, RESUELTO
    private String rejectionReason = ""; // Motivo del rechazo
    private LocalDateTime modificationDeadline = null;// Fecha límite para modificaciones
    private int importanceCount = 0; // Contador de importancia

    public ReportEntity() {
        this.isVerified = false;
        this.status = StatusEnum.PENDING; // Estado inicial por defecto
    }

    public ReportEntity(int id, String title, List<Category> categories, String description, double latitude, double longitude, List<String> images, LocalDateTime date, int userId, List<Comment> comments, String email, String createdByEmail, String location, StatusEnum status, String rejectionReason, LocalDateTime modificationDeadline) {
        this.id = id;
        this.title = title;
        this.categories = categories;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
        this.date = date;
        this.isVerified = false;
        this.userId = userId;
        this.comments = comments;
        this.email = email;
        this.createdByEmail = createdByEmail;
        this.location = location;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.modificationDeadline = modificationDeadline;
    }


}