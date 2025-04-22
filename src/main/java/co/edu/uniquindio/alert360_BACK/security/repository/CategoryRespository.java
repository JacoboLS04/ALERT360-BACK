package co.edu.uniquindio.alert360_BACK.security.repository;

import co.edu.uniquindio.alert360_BACK.security.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoryRespository extends MongoRepository<Category, String> {
    Optional<Category> findByName(String name);
}
