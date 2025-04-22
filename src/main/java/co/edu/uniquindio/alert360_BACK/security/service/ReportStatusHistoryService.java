package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.entity.ReportStatusHistory;
import co.edu.uniquindio.alert360_BACK.security.repository.ReportStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportStatusHistoryService {

    @Autowired
    private ReportStatusHistoryRepository reportStatusHistoryRepository;


    public ReportStatusHistory save(ReportStatusHistory reportStatusHistory) {
        return reportStatusHistoryRepository.save(reportStatusHistory);
    }

    public List<ReportStatusHistory> getStatusHistoryByReportId(int reportId) {
        return reportStatusHistoryRepository.findByReportId(reportId);
    }

}
