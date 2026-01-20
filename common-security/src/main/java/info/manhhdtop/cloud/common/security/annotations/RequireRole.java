package info.manhhdtop.cloud.common.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation để định nghĩa role cần thiết cho controller hoặc method
 * Processor sẽ tự động tạo role nếu chưa tồn tại
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    /**
     * Tên role (bắt buộc)
     */
    String value();

    /**
     * Mô tả role (tùy chọn)
     */
    String description() default "";

    /**
     * Module của role (tùy chọn, mặc định là tên controller)
     */
    String module() default "";
}

