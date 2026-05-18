package info.manhhdtop.cloud.common.core.validations;

import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.utils.MessageUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;
import org.springframework.util.ReflectionUtils;

public class NotSameValueValidator implements ConstraintValidator<NotSameValue, Object> {
    private boolean required;
    private String key1;
    private String key2;

    @Override
    public void initialize(NotSameValue annotation) {
        this.required = annotation.required();
        this.key1 = annotation.key1();
        this.key2 = annotation.key2();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return !required;
        }
        Field field1 = ReflectionUtils.findField(value.getClass(), key1);
        Field field2 = ReflectionUtils.findField(value.getClass(), key2);
        if (field1 == null || field2 == null) {
            return !required;
        }
        ReflectionUtils.makeAccessible(field1);
        ReflectionUtils.makeAccessible(field2);
        Object first = ReflectionUtils.getField(field1, value);
        Object second = ReflectionUtils.getField(field2, value);
        if (first == null || second == null) {
            return !required;
        }
        if (Objects.equals(first, second)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            MessageUtil.get(MessageKeys.VALIDATION_NOT_SAME_VALUE, key1, key2))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
