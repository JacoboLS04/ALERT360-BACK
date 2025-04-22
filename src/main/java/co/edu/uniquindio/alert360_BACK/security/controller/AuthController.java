package co.edu.uniquindio.alert360_BACK.security.controller;

import co.edu.uniquindio.alert360_BACK.security.dto.*;
import co.edu.uniquindio.alert360_BACK.security.entity.UserEntity;
import co.edu.uniquindio.alert360_BACK.security.service.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserEntityService userEntityService;

    @PostMapping("/register")
    public ResponseEntity<MessageDto> create(@Valid @RequestBody CreateUserDto dto){
        try {
            UserEntity userEntity = userEntityService.create(dto);
            return ResponseEntity.ok(new MessageDto(HttpStatus.OK, "User " + userEntity.getUsername() + " created successfully"));
        } catch (RuntimeException e) {
            // Log the exception (you can use a logging framework like SLF4J)
            System.err.println("Error creating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageDto(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user: " + e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<MessageDto> verify(@RequestBody Map<String, String> request) {
        boolean isVerified = userEntityService.verifyUser(request.get("email"), request.get("code"));
        if (isVerified) {
            return ResponseEntity.ok(new MessageDto(HttpStatus.OK, "User verified successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDto(HttpStatus.BAD_REQUEST, "Invalid verification code or code expired."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@Valid @RequestBody LoginUserDto dto) {
        JwtTokenDto jwtTokenDto = userEntityService.login(dto);
        return ResponseEntity.ok(jwtTokenDto);
    }

    @PostMapping("/send-recovery-code")
    public ResponseEntity<MessageDto> sendRecoveryCode(@RequestBody Map<String, String> request) {
        userEntityService.sendRecoveryCode(request.get("email"));
        return ResponseEntity.ok(new MessageDto(HttpStatus.OK, "Recovery code sent successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageDto> resetPassword(@RequestBody Map<String, String> request) {
        userEntityService.resetPassword(request.get("email"), request.get("code"), request.get("newPassword"));
        return ResponseEntity.ok(new MessageDto(HttpStatus.OK, "Password reset successfully"));
    }

    @PostMapping("/update")
    public ResponseEntity<MessageDto> updateUser(@RequestBody @Valid UpdateUserDto dto, @RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            userEntityService.updateUser(email, dto);
            return ResponseEntity.ok(new MessageDto(HttpStatus.OK, "User updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDto(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
}