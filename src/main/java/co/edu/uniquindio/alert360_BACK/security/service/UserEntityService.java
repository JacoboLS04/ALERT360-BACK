package co.edu.uniquindio.alert360_BACK.security.service;

import co.edu.uniquindio.alert360_BACK.security.dto.CreateUserDto;
import co.edu.uniquindio.alert360_BACK.security.dto.JwtTokenDto;
import co.edu.uniquindio.alert360_BACK.security.dto.LoginUserDto;
import co.edu.uniquindio.alert360_BACK.security.dto.UpdateUserDto;
import co.edu.uniquindio.alert360_BACK.security.entity.UserEntity;
import co.edu.uniquindio.alert360_BACK.security.enums.RoleEnum;
import co.edu.uniquindio.alert360_BACK.security.jwt.JwtProvider;
import co.edu.uniquindio.alert360_BACK.security.repository.UserEntityRepositoy;
import co.edu.uniquindio.alert360_BACK.security.utils.MapboxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserEntityService {

    @Autowired
    private UserEntityRepositoy userEntityRepositoy;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    public UserEntity create(CreateUserDto dto) {
        if (userEntityRepositoy.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if (userEntityRepositoy.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El correo ya existe");
        }

        Map<String, Double> coordinates = MapboxUtil.getCoordinates(dto.getAddress());

        UserEntity userEntity = new UserEntity(
                autoIncrementId(),
                dto.getUsername(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getRoles().stream().map(RoleEnum::valueOf).collect(Collectors.toList()),
                generateVerificationCode(),
                new Date(System.currentTimeMillis() + 15 * 60 * 1000), // 15 minutos de expiración
                false,
                dto.getFullName(),
                dto.getCity(),
                dto.getPhone(),
                dto.getAddress(),
                coordinates.get("latitude"),
                coordinates.get("longitude")
        );

        userEntityRepositoy.save(userEntity);
        emailService.sendVerificationEmail(userEntity.getEmail(), userEntity.getVerificationCode());
        return userEntity;
    }


    public boolean verifyUser(String email, String code) {
        UserEntity userEntity = userEntityRepositoy.findByEmail(email);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }
        if (userEntity.getVerificationCode().equals(code) && userEntity.getVerificationExpiry().after(new Date())) {
            userEntity.setActive(true);
            userEntityRepositoy.save(userEntity);
            return true;
        }
        return false;
    }

    public JwtTokenDto login(LoginUserDto dto) {
        UserEntity userEntity = userEntityRepositoy.findByEmail(dto.getEmail());
        if (userEntity == null || !userEntity.isActive()) {
            throw new RuntimeException("User not found or not active");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return new JwtTokenDto(token);
    }

    public UserEntity findByUsername(String username) {
        return userEntityRepositoy.findByUsername(username);
    }

    public UserEntity findByEmail(String email) {
        return userEntityRepositoy.findByEmail(email);
    }

    public void sendRecoveryCode(String email) {
        UserEntity userEntity = userEntityRepositoy.findByEmail(email);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }
        String recoveryCode = generateVerificationCode();
        Date recoveryExpiry = new Date(System.currentTimeMillis() + 15 * 60 * 1000); // 15 minutes expiry
        userEntity.setRecoveryCode(recoveryCode);
        userEntity.setRecoveryExpiry(recoveryExpiry);
        userEntityRepositoy.save(userEntity);
        emailService.sendRecoveryEmail(userEntity.getEmail(), recoveryCode);
    }

    public void resetPassword(String email, String code, String newPassword) {
        UserEntity userEntity = userEntityRepositoy.findByEmail(email);
        if (userEntity == null) {
            throw new RuntimeException("User not found");
        }
        if (userEntity.getRecoveryCode().equals(code) && userEntity.getRecoveryExpiry().after(new Date())) {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userEntity.setRecoveryCode(null);
            userEntity.setRecoveryExpiry(null);
            userEntityRepositoy.save(userEntity);
        } else {
            throw new RuntimeException("Invalid or expired recovery code");
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6 digit code
        return String.valueOf(code);
    }

    private int autoIncrementId() {
        List<UserEntity> users = userEntityRepositoy.findAll();
        return users.isEmpty() ? 1 : users.stream().max(Comparator.comparing(UserEntity::getId)).get().getId() + 1;
    }

    public UserEntity updateUser(String email, UpdateUserDto dto) {
        UserEntity userEntity = userEntityRepositoy.findByEmail(email);
        if (userEntity == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Validaciones de email y username
        if (!userEntity.getEmail().equals(dto.getEmail()) && userEntityRepositoy.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El correo ya está en uso");
        }
        if (!userEntity.getUsername().equals(dto.getUsername()) && userEntityRepositoy.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Actualizar datos del usuario
        userEntity.setUsername(dto.getUsername());
        userEntity.setEmail(dto.getEmail());
        userEntity.setFullName(dto.getFullName());
        userEntity.setCity(dto.getCity());
        userEntity.setPhone(dto.getPhone());
        userEntity.setAddress(dto.getAddress());

        Map<String, Double> coordinates = MapboxUtil.getCoordinates(dto.getAddress());
        userEntity.setLatitude(coordinates.get("latitude"));
        userEntity.setLongitude(coordinates.get("longitude"));

        userEntityRepositoy.save(userEntity);
        return userEntity;
    }

    public void deleteUserByEmail(String email) {
        UserEntity user = userEntityRepositoy.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        userEntityRepositoy.delete(user);
    }
}