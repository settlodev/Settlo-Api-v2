package co.tz.settlo.api.controllers.auth;

import co.tz.settlo.api.JwtHelper;
import co.tz.settlo.api.controllers.user.UserService;
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponse;
import io.sentry.Sentry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authorization Endpoints")
public class AuthResource {
    private static final Logger log = LoggerFactory.getLogger(AuthResource.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthService authService;

    public AuthResource(AuthenticationManager authenticationManager, UserService userService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.authService = authService;
    }

    // @Operation(summary = "Register user")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Registers a user on the system")
    public ResponseEntity<RegistrationResponseDTO> signup(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        UUID newUserId = userService.create(userRegistrationDTO);

        RegistrationResponseDTO userRegResponse = RegistrationResponseDTO.builder()
                .id(newUserId)
                .email(userRegistrationDTO.getEmail())
                .phoneNumber(userRegistrationDTO.getPhoneNumber())
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userRegResponse);
    }

    // @Operation(summary = "Authenticate user and return tokens")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return tokens", description = "Authenticate user and return authentication tokens")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (BadCredentialsException e) {
            authService.addLoginAttempt(request.email(), false);
            Sentry.captureException(e);
            throw e;
        }

        authService.addLoginAttempt(request.email(), true);
        return ResponseEntity.ok(authService.login(request.email(), request.password()));
    }

    // @Operation(summary = "Refresh access token")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/refresh")
    @Operation(summary = "Refresh Access Token", description = "Use refresh token to generate new access token")
    public ResponseEntity<LoginResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(authService.refreshToken(request.refreshToken()));
    }

    // @Operation(summary = "Generate verification token")
    @PutMapping("/generate-verification-token/{email}")
    @Operation(summary = "Generate verification token", description = "Generate verification token to be used to verify email address")
    public ResponseEntity<UUID> generateVerificationToken(@PathVariable String email) {
        return ResponseEntity.ok(userService.generateVerificationToken(email));
    }

    // @Operation(summary = "Verify token")
    @GetMapping("/verify-token/{token}")
    @Operation(summary = "Verify token", description = "Verify generated tokens")
    public ResponseEntity<UUID> verifyToken(@Valid @PathVariable UUID token) {
        return ResponseEntity.ok(userService.verifyToken(token));
    }

    // @Operation(summary = "Get recent login attempts")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginAttemptResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping("/attempts")
    @Operation(summary = "Get recent login attempts", description = "Get recent login attempts by specific user")
    public ResponseEntity<List<LoginAttemptResponse>> loginAttempts(@RequestHeader("Authorization") String token) {
        String fullToken = JwtHelper.getInstance().getTokenFromAuthorizationHeader(token);
        String email = JwtHelper.getInstance().extractUsername(fullToken);
        return ResponseEntity.ok(authService.findRecentLoginAttempts(email).stream()
                .map(LoginAttemptResponse::convertToDTO)
                .toList());
    }

    // @Operation(summary = "Generate password reset token")
    @PostMapping("/reset-password")
    @Operation(summary = "Generate password reset token", description = "Returns password reset token")
    public ResponseEntity<UUID> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.get(resetPasswordDTO.email());
        return ResponseEntity.ok(authService.generatePasswordResetToken(resetPasswordDTO.email()));
    }

    // @Operation(summary = "Update user password")
    @PostMapping("/update-password")
    @Operation(summary = "Update user password", description = "Update user password")
    public ResponseEntity<UUID> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return ResponseEntity.ok(userService.updatePassword(updatePasswordDTO));
    }

    // @Operation(summary = "Logout user")
    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Log user out of all sessions")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String fullToken = JwtHelper.getInstance().getTokenFromAuthorizationHeader(token);
        String email = JwtHelper.getInstance().extractUsername(fullToken);
        authService.deleteByEmail(email);
        return ResponseEntity.ok().build();
    }
}
