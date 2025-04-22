package co.edu.uniquindio.alert360_BACK.security.controller;

import co.edu.uniquindio.alert360_BACK.security.dto.MessageDto;
import co.edu.uniquindio.alert360_BACK.security.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserEntityService userEntityService;

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<MessageDto> deleteAccount(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            userEntityService.deleteUserByEmail(email);
            return ResponseEntity.ok(new MessageDto(HttpStatus.OK, "Account deleted successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageDto(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
}