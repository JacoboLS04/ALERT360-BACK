package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.dto.CategoryDto;
import co.edu.uniquindio.alert360_BACK.security.dto.ReportDto;
import co.edu.uniquindio.alert360_BACK.security.dto.response.ReporteResponseDto;
import co.edu.uniquindio.alert360_BACK.security.entity.Category;
import co.edu.uniquindio.alert360_BACK.security.entity.ReportEntity;
import co.edu.uniquindio.alert360_BACK.security.entity.ReportStatusHistory;
import co.edu.uniquindio.alert360_BACK.security.entity.UserEntity;
import co.edu.uniquindio.alert360_BACK.security.enums.StatusEnum;
import co.edu.uniquindio.alert360_BACK.security.exception.NullDataException;
import co.edu.uniquindio.alert360_BACK.security.exception.ResourceNotFoundException;
import co.edu.uniquindio.alert360_BACK.security.repository.ReportRepository;
import co.edu.uniquindio.alert360_BACK.security.utils.DtoMapper;
import co.edu.uniquindio.alert360_BACK.security.utils.MapboxUtil;
import co.edu.uniquindio.alert360_BACK.security.utils.PdfGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Clase de servicio para la gestión de reportes.
 * Proporciona métodos para crear, actualizar, eliminar y filtrar reportes,
 * así como para gestionar su estado y generar archivos PDF.
 */
@Getter
@Setter
@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private DtoMapper dtoMapper;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private ReportStatusHistoryService reportStatusHistoryService;


    /**
     * Crea un nuevo reporte.
     * @param reportDto El objeto de transferencia de datos que contiene los detalles del reporte.
     * @return Un DTO de respuesta con los detalles del reporte creado.
     */
    public ReporteResponseDto createReport(ReportDto reportDto) {

        if(reportDto == null){
            throw new NullDataException("Estas intentando registrar un reporte nulo");
        }

        Map<String, Double> coordinates;
        try {
            coordinates = MapboxUtil.getCoordinates(reportDto.getLocation());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error al obtener las coordenadas de la ubicación: " + e.getMessage());
        }

        ReportEntity report = new ReportEntity();
        report.setId(autoIncrementId());
        report.setTitle(reportDto.getTitle());
        report.setDescription(reportDto.getDescription());
        report.setLatitude(coordinates.get("latitude"));
        report.setLongitude(coordinates.get("longitude"));
        report.setLocation(reportDto.getLocation());
        report.setImages(reportDto.getImages());
        report.setDate(LocalDateTime.now());
        report.setUserId(reportDto.getUserId());
        report.setEmail(reportDto.getEmail());
        report.setCreatedByEmail(reportDto.getCreatedByEmail());

        report.setCategories(
                reportDto.getCategories().stream()
                        .map(categoryDto -> dtoMapper.mapToEntity(categoryDto, Category.class))
                        .toList()
        );

        reportRepository.save(report);

        return new ReporteResponseDto(report.getId(), report.getTitle(), reportDto.getCategories(),
                report.getDescription(), report.getLocation(), report.getImages(),
                report.getDate(), report.getUserId(), report.getStatus(),report.getImportanceCount());
    }


    /**
     * Actualiza un reporte existente.
     * @param id El ID del reporte a actualizar.
     * @param reportDto El objeto de transferencia de datos que contiene los detalles actualizados del reporte.
     * @return Un DTO de respuesta con los detalles del reporte actualizado.
     */
    public ReporteResponseDto updateReport(int id, ReportDto reportDto) {
        if (reportDto == null) {
            throw new NullDataException("El reporte a actualizar no puede ser nulo");
        }

        // Buscar el reporte existente
        ReportEntity existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con el ID: " + id));

        // Actualizar los campos del reporte
        existingReport.setTitle(reportDto.getTitle());
        existingReport.setDescription(reportDto.getDescription());
        existingReport.setLocation(reportDto.getLocation());
        existingReport.setImages(reportDto.getImages());
        existingReport.setCategories(
                reportDto.getCategories().stream()
                        .map(categoryDto -> dtoMapper.mapToEntity(categoryDto, Category.class))
                        .toList()
        );
        existingReport.setLatitude(reportDto.getLatitude());
        existingReport.setLongitude(reportDto.getLongitude());

        // Guardar el reporte actualizado
        reportRepository.save(existingReport);

        // Retornar el DTO de respuesta
        return new ReporteResponseDto(
                existingReport.getId(),
                existingReport.getTitle(),
                reportDto.getCategories(),
                existingReport.getDescription(),
                existingReport.getLocation(),
                existingReport.getImages(),
                existingReport.getDate(),
                existingReport.getUserId(),
                existingReport.getStatus(),
                reportDto.getImportanceCount()
        );
    }

    /**
     * Elimina un reporte cambiando su estado a RECHAZADO.
     * @param id El ID del reporte a eliminar.
     */
    public void deleteReport(int id) {

        ReportEntity report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con el ID: " + id));

        report.setStatus(StatusEnum.REJECTED);
        reportRepository.save(report);
    }

    /**
     * Cambia el estado de un reporte.
     * @param id El ID del reporte a actualizar.
     * @param newStatus El nuevo estado que se asignará al reporte.
     * @return Un DTO de respuesta con los detalles del reporte actualizado.
     */
    public ReporteResponseDto changeReportStatus(int id, StatusEnum newStatus) {
        if (newStatus == null) {
            throw new NullDataException("El nuevo estado no puede ser nulo");
        }

        if (newStatus == StatusEnum.REJECTED) {
            throw new IllegalArgumentException("Use el método rejectReport para rechazar un reporte");
        }

        ReportEntity existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con el ID: " + id));

        reportStatusHistoryService.save(
                new ReportStatusHistory(
                    existingReport.getId(),
                    existingReport.getStatus(),
                    newStatus,
                    LocalDateTime.now()
                )
        );

        existingReport.setStatus(newStatus);
        existingReport.setRejectionReason(null);
        existingReport.setModificationDeadline(null);

        reportRepository.save(existingReport);

        return new ReporteResponseDto(
                existingReport.getId(),
                existingReport.getTitle(),
                existingReport.getCategories().stream()
                        .map(category -> dtoMapper.mapToDTO(category, CategoryDto.class))
                        .toList(),
                existingReport.getDescription(),
                existingReport.getLocation(),
                existingReport.getImages(),
                existingReport.getDate(),
                existingReport.getUserId(),
                existingReport.getStatus(),
                existingReport.getImportanceCount()
        );
    }

    /**
     * Rechaza un reporte y establece un motivo de rechazo.
     * @param id El ID del reporte a rechazar.
     * @param rejectionReason El motivo del rechazo del reporte.
     * @return Un DTO de respuesta con los detalles del reporte actualizado.
     */
    public ReporteResponseDto rejectReport(int id, String rejectionReason) {
        if (rejectionReason == null || rejectionReason.isEmpty()) {
            throw new NullDataException("Debe proporcionar un motivo de rechazo");
        }

        ReportEntity existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con el ID: " + id));

        reportStatusHistoryService.save(
          new ReportStatusHistory(
                  existingReport.getId(),
                  existingReport.getStatus(),
                  StatusEnum.REJECTED,
                  LocalDateTime.now()
          )
        );
        existingReport.setStatus(StatusEnum.REJECTED);
        existingReport.setRejectionReason(rejectionReason);
        existingReport.setModificationDeadline(LocalDateTime.now().plusDays(5));

        reportRepository.save(existingReport);

        return new ReporteResponseDto(
                existingReport.getId(),
                existingReport.getTitle(),
                existingReport.getCategories().stream()
                        .map(category -> dtoMapper.mapToDTO(category, CategoryDto.class))
                        .toList(),
                existingReport.getDescription(),
                existingReport.getLocation(),
                existingReport.getImages(),
                existingReport.getDate(),
                existingReport.getUserId(),
                existingReport.getStatus(),
                existingReport.getImportanceCount()
        );
    }

    /**
     * Cambia el estado de un reporte a RESUELTO.
     * @param reportId El ID del reporte a actualizar.
     * @return Un DTO de respuesta con los detalles del reporte actualizado.
     */
    public ReporteResponseDto resolveReport(int reportId) {
        // Fetch the report by ID
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con el ID: " + reportId));

        // Save the status change in the history
        reportStatusHistoryService.save(
                new ReportStatusHistory(
                        report.getId(),
                        report.getStatus(),
                        StatusEnum.RESOLVED,
                        LocalDateTime.now()
                )
        );

        // Update the report's status to RESOLVED
        report.setStatus(StatusEnum.RESOLVED);
        report.setRejectionReason(null); // Clear rejection reason if any
        report.setModificationDeadline(null); // Clear modification deadline if any

        // Save the updated report
        reportRepository.save(report);

        // Return the updated report as a DTO
        return new ReporteResponseDto(
                report.getId(),
                report.getTitle(),
                report.getCategories().stream()
                        .map(category -> dtoMapper.mapToDTO(category, CategoryDto.class))
                        .toList(),
                report.getDescription(),
                report.getLocation(),
                report.getImages(),
                report.getDate(),
                report.getUserId(),
                report.getStatus(),
                report.getImportanceCount()
        );
    }

    /**
     * Recupera los reportes asociados a un usuario por su ID.
     * @param userId El ID del usuario.
     * @return Una lista de reportes asociados al usuario.
     */
    public List<ReportEntity> getReportsByUserId(int userId) {
        return reportRepository.findByUserId(userId);
    }

    /**
     * Recupera un reporte por su ID.
     * @param reportId El ID del reporte.
     * @return La entidad del reporte, o null si no se encuentra.
     */
    public ReportEntity getReportById(String reportId) {
        return reportRepository.findById(Integer.parseInt(reportId)).orElse(null);
    }

    /**
     * Recupera el correo electrónico del creador de un reporte.
     * @param reportId El ID del reporte.
     * @return El correo electrónico del creador del reporte.
     */
    public String getReportCreatorEmail(String reportId) {
        ReportEntity report = reportRepository.findById(Integer.parseInt(reportId))
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return report.getCreatedByEmail();
    }

    /**
     * Genera un ID único para un nuevo reporte incrementando el ID más alto existente.
     * @return El ID generado.
     */
    private int autoIncrementId() {
        List<ReportEntity> reports = reportRepository.findAll();
        return reports.isEmpty() ? 1 : reports.stream().max(Comparator.comparing(ReportEntity::getId)).get().getId() + 1;
    }

    /**
     * Recupera reportes basados en varios filtros.
     * @param sector El sector a filtrar.
     * @param category La categoría a filtrar.
     * @param startDate La fecha de inicio del filtro.
     * @param endDate La fecha de fin del filtro.
     * @return Una lista de reportes que cumplen con los filtros.
     */
    public List<ReporteResponseDto> getReportsByFilters(String sector, String category, LocalDateTime startDate, LocalDateTime endDate) {
        return reportRepository.findAll().stream()
                .filter(report -> (sector == null || report.getLocation().contains(sector)) &&
                        (category == null || report.getCategories().stream()
                                .anyMatch(cat -> cat.getName().equalsIgnoreCase(category))) &&
                        (startDate == null || !report.getDate().isBefore(startDate)) &&
                        (endDate == null || !report.getDate().isAfter(endDate)))
                .map(report -> new ReporteResponseDto(
                        report.getId(),
                        report.getTitle(),
                        report.getCategories().stream()
                                .map(cat -> dtoMapper.mapToDTO(cat, CategoryDto.class))
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
    }

    /**
     * Genera un archivo PDF con los detalles de los reportes proporcionados.
     * @param reportes La lista de reportes a incluir en el PDF.
     * @param filePath La ruta del archivo donde se guardará el PDF.
     */
    public void generateReportsPdf(List<ReporteResponseDto> reportes, String filePath) {
        if (reportes == null || reportes.isEmpty()) {
            throw new IllegalArgumentException("La lista de reportes no puede estar vacía o nula.");
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("La ruta del archivo no puede estar vacía o nula.");
        }

        try {
            pdfGenerator.generateReportPdf(reportes, filePath);
        } catch (RuntimeException | FileNotFoundException e) {
            // Registrar el error o manejarlo según sea necesario
            throw new RuntimeException("Error al generar el archivo PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Incrementa el conteo de importancia de un reporte.
     * @param reportId El ID del reporte al que se incrementará la importancia.
     * @return True si la operación fue exitosa, false en caso contrario.
     */
    public boolean increaseImportance(int reportId){
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte no encontrado con el ID: " + reportId));

        report.setImportanceCount(report.getImportanceCount() + 1);
        reportRepository.save(report);

        return true;
    }

    /**
     * Sube imágenes al servidor y devuelve las rutas de las imágenes subidas.
     * @param images La lista de archivos de imágenes a subir.
     * @return Una lista de rutas de las imágenes subidas.
     */
    public List<String> uploadImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        String uploadDir = "uploads/"; // Directorio donde se guardarán las imágenes

        try {
            for (MultipartFile image : images) {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, image.getBytes());
                imageUrls.add(filePath.toString()); // Agregar la ruta de la imagen a la lista
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al subir las imágenes: " + e.getMessage(), e);
        }

        return imageUrls;
    }


}