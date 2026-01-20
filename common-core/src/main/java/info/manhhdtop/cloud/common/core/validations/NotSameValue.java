package info.manhhdtop.cloud.common.core.validations;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotSameValueValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotSameValue {
    String message() default "Value must not same value";

    String key1();

    String key2();

    boolean required() default false;
}
