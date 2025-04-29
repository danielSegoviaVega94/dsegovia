package ds.evaluacion.dsegovia.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ValidacionConfig {

    @Value("${password.validation.regex}")
    private String passwordRegex;

    @Value("${password.validation.message}")
    private String passwordErrorMessage;

}