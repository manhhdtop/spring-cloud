package info.manhhdtop.cloud.common.core.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

public class NotSameValueValidator implements ConstraintValidator<NotSameValue, Object> {
    private boolean required;
    private String key1;
    private String key2;

    @Override
    public void initialize(NotSameValue annotation) {
        ConstraintValidator.super.initialize(annotation);
        this.required = annotation.required();
        this.key1 = annotation.key1();
        this.key2 = annotation.key2();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null) {
            return !required;
        }
        Class<?> oClass = o.getClass();
        Field field1 = ReflectionUtils.findField(oClass, key1);
        Field field2 = ReflectionUtils.findField(oClass, key2);
        if (field1 == null || field2 == null) {
            return !required;
        }
        Object o1 = ReflectionUtils.getField(field1, oClass);
        Object o2 = ReflectionUtils.getField(field2, oClass);
        if (o1 == null || o2 == null) {
            return !required;
        }
        return Objects.equals(o1, o2);
    }
}
