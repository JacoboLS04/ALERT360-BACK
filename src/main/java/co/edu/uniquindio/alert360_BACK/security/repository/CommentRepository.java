package co.edu.uniquindio.alert360_BACK.security.repository;

import co.edu.uniquindio.alert360_BACK.security.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByReportId(String reportId);
}