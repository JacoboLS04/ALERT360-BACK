package co.edu.uniquindio.alert360_BACK.security.entity;

import co.edu.uniquindio.alert360_BACK.security.enums.StatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "report_status_history")
public class ReportStatusHistory {

    @Id
    private String id;

    private int reportId;
    private StatusEnum previousStatus;
    private StatusEnum newStatus;
    private LocalDateTime changeDate;

    public ReportStatusHistory(int reportId, StatusEnum previousStatus, StatusEnum newStatus, LocalDateTime changeDate) {
        this.reportId = reportId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changeDate = changeDate;
    }

}
