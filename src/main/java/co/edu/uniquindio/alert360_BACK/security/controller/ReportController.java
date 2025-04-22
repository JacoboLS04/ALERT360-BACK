package co.edu.uniquindio.alert360_BACK.security.controller;

import co.edu.uniquindio.alert360_BACK.security.dto.ReportDto;
import co.edu.uniquindio.alert360_BACK.security.dto.response.ReporteResponseDto;
import co.edu.uniquindio.alert360_BACK.security.enums.StatusEnum;
import co.edu.uniquindio.alert360_BACK.security.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ReporteResponseDto> createReport(
            @RequestPart("report") @Valid String reportJson,
            @RequestPart("images") List<MultipartFile> images) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ReportDto reportDto = objectMapper.readValue(reportJson, ReportDto.class);
            List<String> imageUrls = reportService.uploadImages(images);
            reportDto.setImages(imageUrls);
            ReporteResponseDto createdReport = reportService.createReport(reportDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponseDto> updateReport(@PathVariable int id, @RequestBody ReportDto reportDto) {
        ReporteResponseDto updatedReport = reportService.updateReport(id, reportDto);
        return ResponseEntity.ok(updatedReport);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable int id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReporteResponseDto> changeReportStatus(@PathVariable int id, @RequestParam StatusEnum newStatus) {
        ReporteResponseDto updatedReport = reportService.changeReportStatus(id, newStatus);
        return ResponseEntity.ok(updatedReport);
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<ReporteResponseDto> resolveReport(@PathVariable int id) {
        ReporteResponseDto resolvedReport = reportService.resolveReport(id);
        return ResponseEntity.ok(resolvedReport);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ReporteResponseDto> rejectReport(@PathVariable int id, @RequestParam String rejectionReason) {
        ReporteResponseDto rejectedReport = reportService.rejectReport(id, rejectionReason);
        return ResponseEntity.ok(rejectedReport);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReporteResponseDto>> getReportsByUserId(@PathVariable int userId) {
        List<ReporteResponseDto> reports = reportService.getReportsByUserId(userId).stream()
                .map(report -> new ReporteResponseDto(
                        report.getId(),
                        report.getTitle(),
                        report.getCategories().stream()
                                .map(category -> reportService.getDtoMapper().mapToDTO(category, co.edu.uniquindio.alert360_BACK.security.dto.CategoryDto.class))
                                .toList(),
                        report.getDescription(),
                        report.getLocation(),
                        report.getImages(),
                        report.getDate(),
                        report.getUserId(),
                        report.getStatus(),
                        report.getImportanceCount()
                ))
                .toList();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDto> getReportById(@PathVariable int id) {
        ReporteResponseDto report = reportService.getReportsByFilters(null, null, null, null).stream().findFirst().orElse(null);
        return ResponseEntity.ok(report);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReporteResponseDto>> getReportsByFilters(
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        List<ReporteResponseDto> reports = reportService.getReportsByFilters(sector, category, startDate, endDate);
        return ResponseEntity.ok(reports);
    }

    @PatchMapping("/{id}/importance")
    public ResponseEntity<Boolean> increaseImportance(@PathVariable int id) {
        boolean success = reportService.increaseImportance(id);
        return ResponseEntity.ok(success);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/generate-pdf")
    public ResponseEntity<String> generateReportsPdf(
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam String filePath) {
        try {
            List<ReporteResponseDto> reports = reportService.getReportsByFilters(sector, category, startDate, endDate);
            reportService.generateReportsPdf(reports, filePath);
            return ResponseEntity.ok("PDF generated successfully at: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF: " + e.getMessage());
        }
    }


}