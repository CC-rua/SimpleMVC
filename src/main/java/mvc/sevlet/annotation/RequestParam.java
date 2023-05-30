package mvc.sevlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RequestParam
 *
 * @author cc
 * @description 请求参数
 * @since 2023/5/26 16:15
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    /**
     * 方法名参数
     *
     * @return String
     */
    String value() default "";

    /**
     * 是否必传
     *
     * @return boolean
     */
    boolean required() default true;
}
