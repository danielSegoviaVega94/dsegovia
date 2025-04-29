package ds.evaluacion.dsegovia.validation;

import ds.evaluacion.dsegovia.config.ValidacionConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private final ValidacionConfig validacionConfig;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        boolean isValid = value.matches(validacionConfig.getPasswordRegex());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(validacionConfig.getPasswordErrorMessage())
                    .addConstraintViolation();
        }

        return isValid;
    }
}