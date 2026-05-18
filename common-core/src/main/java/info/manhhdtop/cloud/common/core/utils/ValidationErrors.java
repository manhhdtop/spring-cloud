package info.manhhdtop.cloud.common.core.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public final class ValidationErrors {

    private ValidationErrors() {
    }

    /**
     * Collects field-level and class-level (e.g. {@code @NotSameValue}) validation messages.
     */
    public static List<String> from(BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.add(formatFieldError(fieldError));
        }
        for (ObjectError globalError : bindingResult.getGlobalErrors()) {
            String message = globalError.getDefaultMessage();
            if (StringUtils.isNotBlank(message)) {
                errors.add(message.strip());
            }
        }
        return errors;
    }

    private static String formatFieldError(FieldError fieldError) {
        String message = fieldError.getDefaultMessage();
        if (StringUtils.isBlank(message)) {
            return fieldError.getField();
        }
        return fieldError.getField() + " " + message.strip();
    }
}
