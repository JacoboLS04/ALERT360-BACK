package co.edu.uniquindio.alert360_BACK.security.repository;

import co.edu.uniquindio.alert360_BACK.security.entity.ReportStatusHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportStatusHistoryRepository extends MongoRepository<ReportStatusHistory, String> {
    List<ReportStatusHistory> findByReportId(int reportId);
}

