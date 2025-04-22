package co.edu.uniquindio.alert360_BACK.security.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import co.edu.uniquindio.alert360_BACK.security.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepositoy extends MongoRepository<UserEntity, Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
}