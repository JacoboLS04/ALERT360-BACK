package co.edu.uniquindio.alert360_BACK.security.entity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "comments")
public class Comment {
    private String id;
    private String reportId;
    private int userId;
    private String content;
    private LocalDateTime createdAt;

    public Comment() {
    }

    public Comment(String id, String reportId, int userId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.reportId = reportId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}