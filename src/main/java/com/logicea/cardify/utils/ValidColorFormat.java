package com.logicea.cardify.utils;

import org.springframework.validation.annotation.Validated;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidColorFormat.ColorFormatValidator.class)
public @interface ValidColorFormat {
    String message() default "Invalid color format";

    Class<?>[] groups() default {};

    Class<? extends jakarta.validation.Payload>[] payload() default {};

    class ColorFormatValidator implements ConstraintValidator<ValidColorFormat, String> {
        private static final Pattern COLOR_PATTERN = Pattern.compile("^#[0-9a-fA-F]{6}$");

        @Override
        public void initialize(ValidColorFormat constraintAnnotation) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value == null || COLOR_PATTERN.matcher(value).matches();
        }
    }
}
