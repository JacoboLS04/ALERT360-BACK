package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.entity.Comment;
import co.edu.uniquindio.alert360_BACK.security.repository.CommentRepository;
import co.edu.uniquindio.alert360_BACK.security.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReportService reportService; // Service to fetch report details

    @Autowired
    private EmailService emailService;

    public Comment addComment(String reportId, int userId, String content) {
        Comment comment = new Comment(null, reportId, userId, content, LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        String reportCreatorEmail = reportService.getReportCreatorEmail(reportId);
        String subject = "Nuevo comentario en tu publicación";
        String body = "Un nuevo comentario fue agregado a tu publicación:\n\n" +
                "\"" + content + "\"\n\n" +
                "Saludos,\nAlert360 Team!";
        emailService.sendVerificationEmail(reportCreatorEmail, body);

        return savedComment;
    }

    public List<Comment> getCommentsByReportId(String reportId) {
        return commentRepository.findByReportId(reportId);
    }
}