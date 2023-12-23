package mate.project.store.dto.user;

import jakarta.validation.constraints.NotBlank;
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
        @NotBlank(message = "Email can't be empty")
        @Email
        String email,
        @NotBlank
        @Password
        @Size(min = 8, max = 35, message = "Password size should be between 8 and 35")
        String password,
        @NotBlank
        @Password
        @Size(min = 8, max = 35, message = "Password size should be between 8 and 35")
        String repeatPassword,
        @NotBlank(message = "First name can't be can't be empty")
        String firstName,
        @NotBlank(message = "Last name can't be empty")
        String lastName,
        @NotBlank(message = "Shipping address can't be empty")
        String shippingAddress
) {
}
