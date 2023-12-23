package mate.project.store.validator.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import mate.project.store.validator.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private static final String REGEX_FOR_PASSWORD =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*_=+-]).{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password != null && Pattern.compile(REGEX_FOR_PASSWORD).matcher(password).matches();
    }
}
