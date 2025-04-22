package co.edu.uniquindio.alert360_BACK.security.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginUserDto {
    @NotBlank(message = "El email del usuario es obligatorio")
    private String email;
    @NotBlank (message = "El nombre de usuario es obligatorio")
    private String password;

    public LoginUserDto() {
    }

    public LoginUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
