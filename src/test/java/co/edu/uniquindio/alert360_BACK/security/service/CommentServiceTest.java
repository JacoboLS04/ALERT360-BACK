package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.entity.Comment;
import co.edu.uniquindio.alert360_BACK.security.repository.CommentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ReportService reportService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private CommentService commentService;

    private List<Comment> testComments;
    private Map<String, List<String>> testReportCreatorEmails;

    private String reportId;
    private int userId;
    private String content;
    private Comment comment;
    private Comment savedComment;
    private String reportCreatorEmail;

    @BeforeEach
    void setUp() {
        // Cargar datos del archivo JSON
        testComments = loadCommentsFromJson();

        // Crear datos para pruebas de reportCreatorEmail
        testReportCreatorEmails = createReportEmails();

        // Configurar datos para pruebas individuales
        reportId = "report123";
        userId = 456;
        content = "Este es un comentario de prueba";
        reportCreatorEmail = testReportCreatorEmails.getOrDefault(reportId, List.of("creador@example.com")).get(0);

        LocalDateTime now = LocalDateTime.now();
        comment = new Comment(null, reportId, userId, content, now);

        // Usar el primer comentario del archivo si existe, o crear uno predeterminado
        Comment firstComment = testComments.stream()
                .filter(c -> c.getReportId().equals(reportId))
                .findFirst()
                .orElse(null);

        savedComment = firstComment != null ?
                firstComment :
                new Comment("comment123", reportId, userId, content, now);
    }

    private List<Comment> loadCommentsFromJson() {
        List<Comment> comments = new ArrayList<>();
        try {
            // Cargar el archivo JSON desde resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("comments-data.json");
            if (inputStream != null) {
                // Configurar ObjectMapper para manejar LocalDateTime
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                // Leer datos como lista de Comment
                comments = objectMapper.readValue(inputStream, new TypeReference<List<Comment>>() {});
                inputStream.close();
            } else {
                System.err.println("No se pudo encontrar el archivo comments-data.json");
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo comments-data.json: " + e.getMessage());
        }
        return comments;
    }

    private Map<String, List<String>> createReportEmails() {
        Map<String, List<String>> reportEmails = new HashMap<>();
        reportEmails.put("report123", List.of("creador@example.com", "jacobo.luengas@uniquindio.edu.co"));
        reportEmails.put("report456", List.of("maria.perez@uniquindio.edu.co", "otro@example.com"));
        reportEmails.put("report789", List.of("andres.perez@uniquindio.edu.co", "tercero@example.com"));
        return reportEmails;
    }

    @Test
    void addComment_ShouldCreateCommentAndSendEmail() {
        // Arrange
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        when(reportService.getReportCreatorEmail(reportId)).thenReturn(reportCreatorEmail);
        doNothing().when(emailService).sendVerificationEmail(anyString(), anyString());

        // Act
        Comment result = commentService.addComment(reportId, userId, content);

        // Assert
        assertNotNull(result);
        assertEquals(savedComment.getId(), result.getId());
        assertEquals(reportId, result.getReportId());
        assertEquals(userId, result.getUserId());

        verify(commentRepository).save(any(Comment.class));
        verify(reportService).getReportCreatorEmail(reportId);

        // Verificar que se envió el correo con el contenido correcto
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendVerificationEmail(emailCaptor.capture(), bodyCaptor.capture());

        assertEquals(reportCreatorEmail, emailCaptor.getValue());
        assertTrue(bodyCaptor.getValue().contains(content));
        assertTrue(bodyCaptor.getValue().contains("Un nuevo comentario fue agregado a tu publicación"));
    }

    @Test
    void getCommentsByReportId_ShouldReturnCommentsForReport() {
        // Filtrar comentarios del JSON para el reportId específico
        List<Comment> commentsForReport = testComments.stream()
                .filter(c -> c.getReportId().equals(reportId))
                .collect(Collectors.toList());

        when(commentRepository.findByReportId(reportId)).thenReturn(commentsForReport);

        // Act
        List<Comment> result = commentService.getCommentsByReportId(reportId);

        // Assert
        assertNotNull(result);
        assertEquals(commentsForReport.size(), result.size());
        assertEquals(commentsForReport, result);
        verify(commentRepository).findByReportId(reportId);
    }

    @Test
    void getCommentsByReportId_WhenNoCommentsExist_ShouldReturnEmptyList() {
        // Arrange
        String emptyReportId = "reportSinComentarios";
        when(commentRepository.findByReportId(emptyReportId)).thenReturn(List.of());

        // Act
        List<Comment> result = commentService.getCommentsByReportId(emptyReportId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository).findByReportId(emptyReportId);
    }

    @Test
    void addComment_ShouldHandleCommentContent() {
        // Buscar un comentario con caracteres especiales en los datos de prueba
        Comment specialContentComment = testComments.stream()
                .filter(c -> c.getContent().contains("caracteres especiales"))
                .findFirst()
                .orElse(new Comment("comment4", "report789", 101, "Comentario con caracteres especiales: !@#$%^&*()", LocalDateTime.now()));

        String specialContent = specialContentComment.getContent();
        String specialReportId = specialContentComment.getReportId();
        int specialUserId = specialContentComment.getUserId();

        String specialReportEmail = testReportCreatorEmails.getOrDefault(specialReportId, List.of("creador@example.com")).get(0);

        when(commentRepository.save(any(Comment.class))).thenReturn(specialContentComment);
        when(reportService.getReportCreatorEmail(specialReportId)).thenReturn(specialReportEmail);

        // Act
        Comment result = commentService.addComment(specialReportId, specialUserId, specialContent);

        // Assert
        assertEquals(specialContent, result.getContent());

        // Verificar que el contenido se pasa correctamente al email
        ArgumentCaptor<String> emailCaptorSpecial = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptorSpecial = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendVerificationEmail(emailCaptorSpecial.capture(), bodyCaptorSpecial.capture());
        assertTrue(bodyCaptorSpecial.getValue().contains(specialContent));
    }

    @Test
    void testMultipleCommentsForDifferentReports() {
        // Obtener todos los reportIds únicos del conjunto de datos
        List<String> uniqueReportIds = testComments.stream()
                .map(Comment::getReportId)
                .distinct()
                .collect(Collectors.toList());

        // Verificar que tenemos al menos 2 reportIds diferentes
        if (uniqueReportIds.size() >= 2) {
            // Probar con cada reportId
            for (String testReportId : uniqueReportIds) {
                // Filtrar comentarios para este reportId específico
                List<Comment> expectedComments = testComments.stream()
                        .filter(c -> c.getReportId().equals(testReportId))
                        .collect(Collectors.toList());

                when(commentRepository.findByReportId(testReportId)).thenReturn(expectedComments);

                List<Comment> result = commentService.getCommentsByReportId(testReportId);

                assertNotNull(result);
                assertEquals(expectedComments.size(), result.size());
                assertEquals(expectedComments, result);
            }

            // Verificar que findByReportId se llamó una vez por cada reportId único
            verify(commentRepository, times(uniqueReportIds.size())).findByReportId(anyString());
        }
    }

    @Test
    void testAddMultipleComments() {
        // Si tenemos al menos 3 comentarios de prueba
        if (testComments.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                Comment testComment = testComments.get(i);
                String testReportId = testComment.getReportId();
                int testUserId = testComment.getUserId();
                String testContent = testComment.getContent();
                String testEmail = testReportCreatorEmails.getOrDefault(testReportId, List.of("default@example.com")).get(0);

                // Configurar mocks
                when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
                when(reportService.getReportCreatorEmail(testReportId)).thenReturn(testEmail);

                // Ejecutar el método a probar
                Comment result = commentService.addComment(testReportId, testUserId, testContent);

                // Verificaciones
                assertNotNull(result);
                assertEquals(testComment.getId(), result.getId());
                assertEquals(testContent, result.getContent());
            }

            // Verificar que save se llamó 3 veces
            verify(commentRepository, times(3)).save(any(Comment.class));
            // Verificar que getReportCreatorEmail se llamó 3 veces
            verify(reportService, times(3)).getReportCreatorEmail(anyString());
            // Verificar que sendVerificationEmail se llamó 3 veces
            verify(emailService, times(3)).sendVerificationEmail(anyString(), anyString());
        }
    }
}