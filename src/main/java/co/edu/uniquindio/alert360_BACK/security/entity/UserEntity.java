package co.edu.uniquindio.alert360_BACK.security.entity;

import co.edu.uniquindio.alert360_BACK.security.enums.RoleEnum;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "users")
public class UserEntity {
    private int id;
    private String username;
    private String email;
    private String password;
    private List<RoleEnum> roles;
    private String verificationCode;
    private Date verificationExpiry;
    private boolean isActive;
    private String recoveryCode;
    private Date recoveryExpiry;
    private String fullName;
    private String city;
    private String phone;
    private String address;
    private Double latitude;
    private Double longitude;

    public UserEntity() {
    }

    public UserEntity(int id, String username, String email, String password, List<RoleEnum> roles, String verificationCode, Date verificationExpiry, boolean isActive, String fullName, String city, String phone, String address, Double latitude, Double longitude) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.verificationCode = verificationCode;
        this.verificationExpiry = verificationExpiry;
        this.isActive = isActive;
        this.fullName = fullName;
        this.city = city;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEnum> roles) {
        this.roles = roles;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Date getVerificationExpiry() {
        return verificationExpiry;
    }

    public void setVerificationExpiry(Date verificationExpiry) {
        this.verificationExpiry = verificationExpiry;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRecoveryCode() {
        return recoveryCode;
    }

    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    public Date getRecoveryExpiry() {
        return recoveryExpiry;
    }

    public void setRecoveryExpiry(Date recoveryExpiry) {
        this.recoveryExpiry = recoveryExpiry;
    }

    public String getFullName() {return fullName;}

    public void setFullName(String fullName) {this.fullName = fullName;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public Double getLatitude() {return latitude;}

    public void setLatitude(Double latitude) {this.latitude = latitude;}

    public Double getLongitude() {return longitude;}

    public void setLongitude(Double longitude) {this.longitude = longitude;}
}
