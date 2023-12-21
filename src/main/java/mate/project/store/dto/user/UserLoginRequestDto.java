package mate.project.store.dto.user;

import jakarta.validation.constraints.NotNull;
import mate.project.store.validator.Email;
import mate.project.store.validator.Password;

public record UserLoginRequestDto(
        @NotNull
        @Email
        String email,
        @Password
        String password
) {
}
