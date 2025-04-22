package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.dto.ReportDto;
import co.edu.uniquindio.alert360_BACK.security.entity.ReportEntity;
import co.edu.uniquindio.alert360_BACK.security.enums.StatusEnum;
import co.edu.uniquindio.alert360_BACK.security.repository.ReportRepository;
import co.edu.uniquindio.alert360_BACK.security.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @Test
    public void deleteReportTest() throws Exception {
        ReportEntity report = new ReportEntity();
        report.setId(2);
        report.setTitle("To Be Deleted");
        report.setStatus(StatusEnum.PENDING); // Replaced ACTIVE with PENDING
        reportRepository.save(report);

        assertDoesNotThrow(() -> reportService.deleteReport(report.getId()));

        Optional<ReportEntity> deletedReport = reportRepository.findById(report.getId());
        assertTrue(deletedReport.isPresent());
        assertEquals(StatusEnum.REJECTED, deletedReport.get().getStatus());
        reportRepository.deleteById(report.getId());
    }

    @Test
    public void changeReportStatusTest() throws Exception {
        ReportEntity report = new ReportEntity();
        report.setId(6);
        report.setTitle("Change Status Test");
        report.setStatus(StatusEnum.PENDING); // Replaced ACTIVE with PENDING
        reportRepository.save(report);

        assertDoesNotThrow(() -> reportService.changeReportStatus(report.getId(), StatusEnum.RESOLVED)); // Replaced IN_PROGRESS with RESOLVED

        Optional<ReportEntity> updatedReport = reportRepository.findById(report.getId());
        assertTrue(updatedReport.isPresent());
        assertEquals(StatusEnum.RESOLVED, updatedReport.get().getStatus());
        reportRepository.deleteById(report.getId());
    }

    @Test
    public void rejectReportTest() throws Exception {
        ReportEntity report = new ReportEntity();
        report.setId(7);
        report.setTitle("Report to Reject");
        report.setStatus(StatusEnum.PENDING);
        reportRepository.save(report);

        String rejectionReason = "Invalid data provided";

        assertDoesNotThrow(() -> reportService.rejectReport(report.getId(), rejectionReason));

        Optional<ReportEntity> rejectedReport = reportRepository.findById(report.getId());
        assertTrue(rejectedReport.isPresent());
        assertEquals(StatusEnum.REJECTED, rejectedReport.get().getStatus());
        assertEquals(rejectionReason, rejectedReport.get().getRejectionReason());
        reportRepository.deleteById(report.getId());
    }

    @Test
    public void getReportsByUserIdTest() throws Exception {
        ReportEntity report1 = new ReportEntity();
        report1.setId(8);
        report1.setTitle("User Report 1");
        report1.setUserId(1);
        reportRepository.save(report1);

        ReportEntity report2 = new ReportEntity();
        report2.setId(9);
        report2.setTitle("User Report 2");
        report2.setUserId(1);
        reportRepository.save(report2);

        List<ReportEntity> reports = reportService.getReportsByUserId(1);
        assertEquals(2, reports.size());
        reportRepository.deleteAll(List.of(report1, report2));
    }

    @Test
    public void getReportByIdTest() throws Exception {
        ReportEntity report = new ReportEntity();
        report.setId(10);
        report.setTitle("Get Report Test");
        reportRepository.save(report);

        ReportEntity fetchedReport = reportService.getReportById(String.valueOf(report.getId()));
        assertNotNull(fetchedReport);
        assertEquals(report.getId(), fetchedReport.getId());
        reportRepository.deleteById(report.getId());
    }

    @Test
    public void increaseImportanceTest() throws Exception {
        ReportEntity report = new ReportEntity();
        report.setId(11);
        report.setTitle("Increase Importance Test");
        report.setImportanceCount(0);
        reportRepository.save(report);

        assertTrue(reportService.increaseImportance(report.getId()));

        Optional<ReportEntity> updatedReport = reportRepository.findById(report.getId());
        assertTrue(updatedReport.isPresent());
        assertEquals(1, updatedReport.get().getImportanceCount());
        reportRepository.deleteById(report.getId());
    }


}