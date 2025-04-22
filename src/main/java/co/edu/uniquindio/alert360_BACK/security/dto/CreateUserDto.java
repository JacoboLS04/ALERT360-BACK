package co.edu.uniquindio.alert360_BACK.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class CreateUserDto {
    @NotBlank (message = "El nombre de usuario es obligatorio")
    private String username;
    @NotBlank(message = "El correo es obligatorio")
    private String email;
    @NotBlank (message = "El nombre de usuario es obligatorio")
    private String password;
    @NotEmpty(message = "La lista de roles no puede estar vacía")
    List<String> roles = new ArrayList<>();
    @NotBlank
    private String fullName;
    @NotBlank
    private String city;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;

    public CreateUserDto() {
    }

    public CreateUserDto(String username, String email, String password, List<String> roles, String fullName, String city, String phone, String address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.fullName = fullName;
        this.city = city;
        this.phone = phone;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getFullName() {return fullName;}

    public void setFullName(String fullName) {this.fullName = fullName;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}
}
