package co.edu.uniquindio.alert360_BACK.security.controller;

import co.edu.uniquindio.alert360_BACK.security.entity.Comment;
import co.edu.uniquindio.alert360_BACK.security.entity.ReportEntity;
import co.edu.uniquindio.alert360_BACK.security.entity.UserEntity;
import co.edu.uniquindio.alert360_BACK.security.service.CommentService;
import co.edu.uniquindio.alert360_BACK.security.service.ReportService;
import co.edu.uniquindio.alert360_BACK.security.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private ReportService reportService;

    @PostMapping("/add")
    public ResponseEntity<Comment> addComment(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        UserEntity user = userEntityService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String reportId = request.get("reportId");
        String content = request.get("content");
        Comment comment = commentService.addComment(reportId, user.getId(), content);
        ReportEntity report = reportService.getReportById(reportId);
        report.getComments().add(comment);
        //reportService.updateReport(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping("/report/{reportId}")
    public ResponseEntity<List<Comment>> getCommentsByReportId(@PathVariable String reportId) {
        List<Comment> comments = commentService.getCommentsByReportId(reportId);
        return ResponseEntity.ok(comments);
    }
}