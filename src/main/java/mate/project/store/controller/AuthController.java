package mate.project.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.project.store.dto.user.UserRegistrationRequestDto;
import mate.project.store.dto.user.UserResponseDto;
import mate.project.store.exception.RegistrationException;
import mate.project.store.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
