package com.example.authservice.service.impl;

import com.example.authservice.request.LoginRequestDto;
import com.example.authservice.request.UserRegistrationDto;
import com.example.authservice.response.LoginResponseDto;
import com.example.authservice.service.UserService;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor; // Thêm cái này
import lombok.extern.slf4j.Slf4j;      // Thêm cái này để dùng log
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value; // Sửa lại cái này
import org.springframework.stereotype.Service;              // Thêm cái này

import java.util.Collections;

@Service                // Bổ sung
@Slf4j                  // Bổ sung để hết lỗi 'log'
@RequiredArgsConstructor // Bổ sung để inject Keycloak bean
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Override
    public void createUser(UserRegistrationDto dto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setEmailVerified(true);


        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.getPassword());
        user.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = keycloak.realm(realm).users();


        try (Response response = usersResource.create(user)) {
            if (response.getStatus() != 201) {
                String errorMsg = response.readEntity(String.class);
                log.error("Error creating user, status: {}", response.getStatusInfo().getReasonPhrase());
                throw new RuntimeException("Failed to create user in Keycloak");
            }
            log.info("User created successfully in Keycloak with username: {}", dto.getUsername());
        } catch (Exception e) {
            log.error("Exception when creating user: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        // Sử dụng try-with-resources để tự động đóng Keycloak client sau khi dùng
        try (Keycloak keycloakClient = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(dto.getUsername())
                .password(dto.getPassword())
                .grantType(OAuth2Constants.PASSWORD)
                .build()) {

            // Lấy thông tin token từ Keycloak
            AccessTokenResponse accessTokenResponse = keycloakClient.tokenManager().getAccessToken();

            // Trả về DTO chứa access token và refresh token
            return LoginResponseDto.builder()
                    .accessToken(accessTokenResponse.getToken())
                    .refreshToken(accessTokenResponse.getRefreshToken())
                    .build();

        } catch (NotAuthorizedException e) {
            // Bắn lỗi nếu sai username/password hoặc không có quyền
            throw new RuntimeException("Invalid credentials", e);
        } catch (Exception e) {
            // Xử lý các lỗi hệ thống khác (kết nối, cấu hình...)
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }

}