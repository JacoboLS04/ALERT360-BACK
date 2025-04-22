package co.edu.uniquindio.alert360_BACK.security.repository;

import co.edu.uniquindio.alert360_BACK.security.entity.ReportEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<ReportEntity, Integer> {
    ReportEntity save(ReportEntity report);

    List<ReportEntity> findByUserId(int userId);

}