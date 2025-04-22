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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEntityServiceTest {

    @Mock
    private UserEntityRepositoy userEntityRepositoy;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserEntityService userEntityService;

    private List<UserEntity> testUsers;
    private List<LoginUserDto> testLoginData;
    private List<CreateUserDto> testCreateUserData;
    private List<UpdateUserDto> testUpdateUserData;
    private List<JwtTokenDto> testJwtTokenData;

    private CreateUserDto createUserDto;
    private UserEntity userEntity;
    private UpdateUserDto updateUserDto;
    private LoginUserDto loginUserDto;
    private JwtTokenDto jwtTokenDto;

    @BeforeEach
    void setUp() {
        // Cargar datos del archivo JSON
        testUsers = loadUsersFromJson();

        // Generar datasets adicionales basados en los usuarios cargados
        testLoginData = createLoginData();
        testCreateUserData = createCreateUserData();
        testUpdateUserData = createUpdateUserData();
        testJwtTokenData = createJwtTokenData();

        // Configurar datos para pruebas individuales
        userEntity = testUsers.isEmpty() ? createDefaultUser() : testUsers.get(0);
        createUserDto = testCreateUserData.isEmpty() ? createDefaultCreateUserDto() : testCreateUserData.get(0);
        updateUserDto = testUpdateUserData.isEmpty() ? createDefaultUpdateUserDto() : testUpdateUserData.get(0);
        loginUserDto = testLoginData.isEmpty() ? createDefaultLoginUserDto() : testLoginData.get(0);
        jwtTokenDto = testJwtTokenData.isEmpty() ? new JwtTokenDto("test.jwt.token") : testJwtTokenData.get(0);
    }

    private List<UserEntity> loadUsersFromJson() {
        List<UserEntity> users = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data.json");

            if (inputStream == null) {
                System.err.println("No se pudo encontrar el archivo data.json");
                return users;
            }

            List<Map<String, Object>> usersData = objectMapper.readValue(inputStream, new TypeReference<List<Map<String, Object>>>() {});

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

            for (Map<String, Object> userData : usersData) {
                UserEntity user = new UserEntity();
                user.setId((int) Long.parseLong(userData.get("_id").toString()));
                user.setUsername(userData.get("username").toString());
                user.setEmail(userData.get("email").toString());
                user.setPassword(userData.get("password").toString());

                // Procesar roles (convertir strings a enums)
                @SuppressWarnings("unchecked")
                List<String> roleStrings = (List<String>) userData.get("roles");
                List<RoleEnum> roles = new ArrayList<>();
                for (String roleString : roleStrings) {
                    roles.add(RoleEnum.valueOf(roleString));
                }
                user.setRoles(roles);

                user.setVerificationCode(userData.get("verificationCode").toString());

                try {
                    user.setVerificationExpiry(dateFormat.parse(userData.get("verificationExpiry").toString()));
                } catch (ParseException e) {
                    user.setVerificationExpiry(new Date());
                }

                user.setActive((Boolean) userData.get("isActive"));
                user.setFullName(userData.get("fullName").toString());
                user.setCity(userData.get("city").toString());
                user.setPhone(userData.get("phone").toString());
                user.setAddress(userData.get("address").toString());
                user.setLatitude(Double.valueOf(Double.parseDouble(userData.get("latitude").toString())));
                user.setLongitude(Double.valueOf(Double.parseDouble(userData.get("longitude").toString())));

                users.add(user);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar datos del archivo JSON: " + e.getMessage());
        }
        return users;
    }

    private List<LoginUserDto> createLoginData() {
        List<LoginUserDto> logins = new ArrayList<>();

        for (UserEntity user : testUsers) {
            LoginUserDto login = new LoginUserDto();
            login.setEmail(user.getEmail());
            login.setPassword("password123"); // Contraseña simulada para pruebas
            logins.add(login);
        }

        return logins;
    }

    private List<CreateUserDto> createCreateUserData() {
        List<CreateUserDto> createDtos = new ArrayList<>();

        // Primer usuario
        List<String> roles1 = List.of("ROLE_USER");
        CreateUserDto user1 = new CreateUserDto();
        user1.setUsername("nuevoUsuario1");
        user1.setEmail("nuevo1@uniquindio.edu.co");
        user1.setPassword("password123");
        user1.setFullName("Nuevo Usuario Uno");
        user1.setCity("Armenia");
        user1.setPhone("3001112233");
        user1.setAddress("Calle 15 #20-30, Armenia, Quindío");
        user1.setRoles(roles1);
        createDtos.add(user1);

        // Segundo usuario
        List<String> roles2 = List.of("ROLE_ADMIN");
        CreateUserDto user2 = new CreateUserDto();
        user2.setUsername("nuevoUsuario2");
        user2.setEmail("nuevo2@uniquindio.edu.co");
        user2.setPassword("password456");
        user2.setFullName("Nuevo Usuario Dos");
        user2.setCity("Pereira");
        user2.setPhone("3002223344");
        user2.setAddress("Avenida 30 #15-25, Pereira, Risaralda");
        user2.setRoles(roles2);
        createDtos.add(user2);

        // Tercer usuario
        List<String> roles3 = List.of("ROLE_USER", "ROLE_ADMIN");
        CreateUserDto user3 = new CreateUserDto();
        user3.setUsername("nuevoUsuario3");
        user3.setEmail("nuevo3@uniquindio.edu.co");
        user3.setPassword("password789");
        user3.setFullName("Nuevo Usuario Tres");
        user3.setCity("Manizales");
        user3.setPhone("3003334455");
        user3.setAddress("Carrera 22 #45-67, Manizales, Caldas");
        user3.setRoles(roles3);
        createDtos.add(user3);

        return createDtos;
    }

    private List<UpdateUserDto> createUpdateUserData() {
        List<UpdateUserDto> updateDtos = new ArrayList<>();

        // Actualización para usuario 1
        UpdateUserDto update1 = new UpdateUserDto();
        update1.setUsername("jacoboActualizado");
        update1.setEmail("jacobo.actualizado@uniquindio.edu.co");
        update1.setFullName("Jacobo Luengas Actualizado");
        update1.setCity("Cali");
        update1.setPhone("3123456780");
        update1.setAddress("Cra 15 #20-45, Cali, Valle");
        updateDtos.add(update1);

        // Actualización para usuario 2
        UpdateUserDto update2 = new UpdateUserDto();
        update2.setUsername("mariaActualizada");
        update2.setEmail("maria.actualizada@uniquindio.edu.co");
        update2.setFullName("Maria Perez Actualizada");
        update2.setCity("Armenia");
        update2.setPhone("3109876540");
        update2.setAddress("Calle 20 #25-50, Armenia, Quindío");
        updateDtos.add(update2);

        // Actualización para usuario 3
        UpdateUserDto update3 = new UpdateUserDto();
        update3.setUsername("andresActualizado");
        update3.setEmail("andres.actualizado@uniquindio.edu.co");
        update3.setFullName("Andres Perez Actualizado");
        update3.setCity("Armenia");
        update3.setPhone("3201234560");
        update3.setAddress("Av Bolívar #15-25, Armenia, Quindío");
        updateDtos.add(update3);

        return updateDtos;
    }

    private List<JwtTokenDto> createJwtTokenData() {
        List<JwtTokenDto> tokenData = new ArrayList<>();

        tokenData.add(new JwtTokenDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqYWNvYm9wdCIsImV4cCI6MTcyMzQ1Njc4OX0.token1"));
        tokenData.add(new JwtTokenDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtYXJpYXB0IiwiZXhwIjoxNzIzNDU2Nzg5fQ.token2"));
        tokenData.add(new JwtTokenDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhbmRyZXNwdCIsImV4cCI6MTcyMzQ1Njc4OX0.token3"));
        tokenData.add(new JwtTokenDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJsdWlzYXF0IiwiZXhwIjoxNzIzNDU2Nzg5fQ.token4"));
        tokenData.add(new JwtTokenDto("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjYW1pbG9wdCIsImV4cCI6MTcyMzQ1Njc4OX0.token5"));

        return tokenData;
    }

    private UserEntity createDefaultUser() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("defaultUser");
        user.setEmail("default@example.com");
        user.setPassword("encodedPassword");
        user.setRoles(List.of(RoleEnum.ROLE_USER));
        user.setVerificationCode("123456");
        user.setVerificationExpiry(new Date(System.currentTimeMillis() + 15 * 60 * 1000));
        user.setActive(true);
        user.setFullName("Default User");
        user.setCity("Default City");
        user.setPhone("1234567890");
        user.setAddress("Default Address");
        user.setLatitude(Double.valueOf(4.5));
        user.setLongitude(Double.valueOf(-75.6));
        return user;
    }

    private CreateUserDto createDefaultCreateUserDto() {
        CreateUserDto dto = new CreateUserDto();
        dto.setUsername("defaultNewUser");
        dto.setEmail("defaultnew@example.com");
        dto.setPassword("password123");
        dto.setFullName("Default New User");
        dto.setCity("Default City");
        dto.setPhone("1234567890");
        dto.setAddress("Default Address");
        dto.setRoles(List.of("ROLE_USER"));
        return dto;
    }

    private UpdateUserDto createDefaultUpdateUserDto() {
        UpdateUserDto dto = new UpdateUserDto();
        dto.setUsername("updatedUser");
        dto.setEmail("updated@example.com");
        dto.setFullName("Updated User");
        dto.setCity("Updated City");
        dto.setPhone("0987654321");
        dto.setAddress("Updated Address");
        return dto;
    }

    private LoginUserDto createDefaultLoginUserDto() {
        LoginUserDto dto = new LoginUserDto();
        dto.setEmail("default@example.com");
        dto.setPassword("password123");
        return dto;
    }

    @Test
    void create_Success() {
        // Configuración
        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("latitude", Double.valueOf(1.234));
        coordinates.put("longitude", Double.valueOf(4.321));

        try (MockedStatic<MapboxUtil> mockedMapboxUtil = Mockito.mockStatic(MapboxUtil.class)) {
            mockedMapboxUtil.when(() -> MapboxUtil.getCoordinates(anyString()))
                    .thenReturn(coordinates);

            when(userEntityRepositoy.existsByUsername(anyString())).thenReturn(Boolean.valueOf(false));
            when(userEntityRepositoy.existsByEmail(anyString())).thenReturn(Boolean.valueOf(false));
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userEntityRepositoy.findAll()).thenReturn(Collections.emptyList());
            when(userEntityRepositoy.save(any(UserEntity.class))).thenReturn(userEntity);
            doNothing().when(emailService).sendVerificationEmail(anyString(), anyString());

            // Ejecución
            UserEntity result = userEntityService.create(createUserDto);

            // Verificación
            assertNotNull(result);
            verify(userEntityRepositoy).existsByUsername(eq(createUserDto.getUsername()));
            verify(userEntityRepositoy).existsByEmail(eq(createUserDto.getEmail()));
            verify(passwordEncoder).encode(eq(createUserDto.getPassword()));
            verify(userEntityRepositoy).save(any(UserEntity.class));
            verify(emailService).sendVerificationEmail(anyString(), anyString());
        }
    }

    @Test
    void create_UsernameExists() {
        when(userEntityRepositoy.existsByUsername(createUserDto.getUsername())).thenReturn(Boolean.valueOf(true));

        // Verificación
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userEntityService.create(createUserDto);
        });

        assertEquals("El nombre de usuario ya existe", exception.getMessage());
        verify(userEntityRepositoy).existsByUsername(createUserDto.getUsername());
        verify(userEntityRepositoy, never()).save(any(UserEntity.class));
    }

    @Test
    void create_EmailExists() {
        when(userEntityRepositoy.existsByUsername(createUserDto.getUsername())).thenReturn(Boolean.valueOf(false));
        when(userEntityRepositoy.existsByEmail(createUserDto.getEmail())).thenReturn(Boolean.valueOf(true));

        // Verificación
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userEntityService.create(createUserDto);
        });

        assertEquals("El correo ya existe", exception.getMessage());
        verify(userEntityRepositoy).existsByUsername(createUserDto.getUsername());
        verify(userEntityRepositoy).existsByEmail(createUserDto.getEmail());
        verify(userEntityRepositoy, never()).save(any(UserEntity.class));
    }

    @Test
    void verifyUser_Success() {
        userEntity.setVerificationExpiry(new Date(System.currentTimeMillis() + 15 * 60 * 1000));
        userEntity.setActive(false);

        when(userEntityRepositoy.findByEmail(userEntity.getEmail())).thenReturn(userEntity);

        // Simular la activación del usuario
        doAnswer(invocation -> {
            UserEntity u = invocation.getArgument(0);
            u.setActive(true);
            return u;
        }).when(userEntityRepositoy).save(any(UserEntity.class));

        boolean result = userEntityService.verifyUser(userEntity.getEmail(), userEntity.getVerificationCode());

        assertTrue(result);
        assertTrue(userEntity.isActive());
        verify(userEntityRepositoy).findByEmail(userEntity.getEmail());
        verify(userEntityRepositoy).save(userEntity);
    }

    @Test
    void verifyUser_UserNotFound() {
        when(userEntityRepositoy.findByEmail("nonexistent@example.com")).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userEntityService.verifyUser("nonexistent@example.com", "123456");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void login_Success() {
        // Usuario activo para la prueba de login
        userEntity.setActive(true);

        Authentication auth = mock(Authentication.class);
        when(userEntityRepositoy.findByEmail(loginUserDto.getEmail())).thenReturn(userEntity);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtProvider.generateToken(auth)).thenReturn(jwtTokenDto.getToken());

        JwtTokenDto result = userEntityService.login(loginUserDto);

        assertNotNull(result);
        assertEquals(jwtTokenDto.getToken(), result.getToken());
    }

    @Test
    void login_UserNotFound() {
        when(userEntityRepositoy.findByEmail("nonexistent@example.com")).thenReturn(null);

        LoginUserDto nonExistentLogin = new LoginUserDto();
        nonExistentLogin.setEmail("nonexistent@example.com");
        nonExistentLogin.setPassword("password123");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userEntityService.login(nonExistentLogin);
        });

        assertEquals("User not found or not active", exception.getMessage());
    }

    @Test
    void updateUser_Success() {
        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("latitude", Double.valueOf(2.345));
        coordinates.put("longitude", Double.valueOf(5.432));

        try (MockedStatic<MapboxUtil> mockedMapboxUtil = Mockito.mockStatic(MapboxUtil.class)) {
            mockedMapboxUtil.when(() -> MapboxUtil.getCoordinates(anyString()))
                    .thenReturn(coordinates);

            when(userEntityRepositoy.findByEmail(userEntity.getEmail())).thenReturn(userEntity);
            when(userEntityRepositoy.save(any(UserEntity.class))).thenReturn(userEntity);

            UserEntity result = userEntityService.updateUser(userEntity.getEmail(), updateUserDto);

            assertNotNull(result);
            assertEquals(updateUserDto.getUsername(), result.getUsername());
            assertEquals(updateUserDto.getEmail(), result.getEmail());
            verify(userEntityRepositoy).save(userEntity);
        }
    }

    @Test
    void deleteUserByEmail_Success() {
        when(userEntityRepositoy.findByEmail(userEntity.getEmail())).thenReturn(userEntity);
        doNothing().when(userEntityRepositoy).delete(userEntity);

        userEntityService.deleteUserByEmail(userEntity.getEmail());

        verify(userEntityRepositoy).findByEmail(userEntity.getEmail());
        verify(userEntityRepositoy).delete(userEntity);
    }

    @Test
    void deleteUserByEmail_UserNotFound() {
        when(userEntityRepositoy.findByEmail("nonexistent@example.com")).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userEntityService.deleteUserByEmail("nonexistent@example.com");
        });

        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void testWithMultipleUsers() {
        if (testUsers.size() >= 2) {
            UserEntity testUser1 = testUsers.get(0);
            UserEntity testUser2 = testUsers.get(1);

            when(userEntityRepositoy.findByEmail(testUser1.getEmail())).thenReturn(testUser1);
            when(userEntityRepositoy.findByEmail(testUser2.getEmail())).thenReturn(testUser2);
            when(userEntityRepositoy.save(any(UserEntity.class))).thenReturn(testUser1, testUser2);

            UserEntity result1 = userEntityService.updateUser(testUser1.getEmail(), testUpdateUserData.get(0));

            assertNotNull(result1);
            assertEquals(testUpdateUserData.get(0).getUsername(), testUser1.getUsername());

            UserEntity result2 = userEntityService.updateUser(testUser2.getEmail(), testUpdateUserData.get(1));

            assertNotNull(result2);
            assertEquals(testUpdateUserData.get(1).getUsername(), testUser2.getUsername());

            verify(userEntityRepositoy, times(2)).save(any(UserEntity.class));
        }
    }

    @Test
    void verifyMultipleUsers() {
        if (testUsers.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                UserEntity testUser = testUsers.get(i);
                testUser.setActive(false);
                testUser.setVerificationExpiry(new Date(System.currentTimeMillis() + 15 * 60 * 1000));

                when(userEntityRepositoy.findByEmail(testUser.getEmail())).thenReturn(testUser);

                doAnswer(invocation -> {
                    UserEntity u = invocation.getArgument(0);
                    u.setActive(true);
                    return u;
                }).when(userEntityRepositoy).save(testUser);

                boolean result = userEntityService.verifyUser(testUser.getEmail(), testUser.getVerificationCode());

                assertTrue(result);
                assertTrue(testUser.isActive());
            }

            verify(userEntityRepositoy, times(3)).findByEmail(anyString());
            verify(userEntityRepositoy, times(3)).save(any(UserEntity.class));
        }
    }

    @Test
    void testMultipleLogins() {
        if (testLoginData.size() >= 3 && testUsers.size() >= 3 && testJwtTokenData.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                LoginUserDto login = testLoginData.get(i);
                UserEntity user = testUsers.get(i);
                user.setActive(true);
                String token = testJwtTokenData.get(i).getToken();

                Authentication auth = mock(Authentication.class);
                when(userEntityRepositoy.findByEmail(login.getEmail())).thenReturn(user);
                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
                when(jwtProvider.generateToken(auth)).thenReturn(token);

                JwtTokenDto result = userEntityService.login(login);

                assertNotNull(result);
                assertEquals(token, result.getToken());
            }

            verify(userEntityRepositoy, times(3)).findByEmail(anyString());
            verify(authenticationManager, times(3)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtProvider, times(3)).generateToken(any(Authentication.class));
        }
    }
}