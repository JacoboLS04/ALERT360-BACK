package co.edu.uniquindio.alert360_BACK.security.controller;

import co.edu.uniquindio.alert360_BACK.security.entity.ReportStatusHistory;
import co.edu.uniquindio.alert360_BACK.security.service.ReportStatusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report-status-history")
public class ReportStatusHistoryController {

    @Autowired
    private ReportStatusHistoryService reportStatusHistoryService;

    @PostMapping
    public ResponseEntity<ReportStatusHistory> saveReportStatusHistory(@RequestBody ReportStatusHistory reportStatusHistory) {
        ReportStatusHistory savedHistory = reportStatusHistoryService.save(reportStatusHistory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHistory);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<List<ReportStatusHistory>> getStatusHistoryByReportId(@PathVariable int reportId) {
        List<ReportStatusHistory> historyList = reportStatusHistoryService.getStatusHistoryByReportId(reportId);
        return ResponseEntity.ok(historyList);
    }
}