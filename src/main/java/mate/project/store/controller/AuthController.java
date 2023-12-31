package mate.project.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.user.UserLoginRequestDto;
import mate.project.store.dto.user.UserLoginResponseDto;
import mate.project.store.dto.user.UserRegistrationRequestDto;
import mate.project.store.dto.user.UserResponseDto;
import mate.project.store.exception.RegistrationException;
import mate.project.store.security.AuthenticationService;
import mate.project.store.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management", description = "Endpoints for managing authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Registration a new user",
            description = "Registration a new user")
    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @Operation(summary = "Login user",
            description = "Authorization of user")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }
}
