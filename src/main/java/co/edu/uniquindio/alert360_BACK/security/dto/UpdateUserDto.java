package co.edu.uniquindio.alert360_BACK.security.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateUserDto {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;
    @NotBlank(message = "El correo es obligatorio")
    private String email;
    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;
    @NotBlank(message = "La ciudad es obligatoria")
    private String city;
    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;
    @NotBlank(message = "La dirección es obligatoria")
    private String address;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setAddress(String address) {
    this.address = address;
    }

    public String getAddress() {
        return address;
    }

}

