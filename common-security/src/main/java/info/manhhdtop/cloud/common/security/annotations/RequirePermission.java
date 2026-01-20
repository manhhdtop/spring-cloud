package info.manhhdtop.cloud.common.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation để định nghĩa permission cần thiết cho controller hoặc method
 * Processor sẽ tự động tạo permission nếu chưa tồn tại
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    /**
     * Tên permission (bắt buộc)
     */
    String value();

    /**
     * Mô tả permission (tùy chọn)
     */
    String description() default "";

    /**
     * Module của permission (tùy chọn, mặc định là tên controller)
     */
    String module() default "";
}

