package mate.project.store.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mate.project.store.validator.Email;
import mate.project.store.validator.FieldMatch;
import mate.project.store.validator.Password;

@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords must match"
)
public record UserRegistrationRequestDto(
        @NotNull(message = "Email can't be null")
        @NotBlank(message = "Email can't be empty")
        @Email
        String email,
        @NotNull
        @NotBlank
        @Password
        @Size(min = 8, max = 35, message = "Password size should be between 8 and 35")
        String password,
        @NotNull
        @NotBlank
        @Password
        @Size(min = 8, max = 35, message = "Password size should be between 8 and 35")
        String repeatPassword,
        @NotNull(message = "First name can't be null")
        @NotBlank(message = "First name can't be can't be empty")
        String firstName,
        @NotNull(message = "Last name can't be null")
        @NotBlank(message = "Last name can't be empty")
        String lastName,
        @NotNull(message = "Shipping address can't be null")
        @NotBlank(message = "Shipping address can't be empty")
        String shippingAddress
) {
}
