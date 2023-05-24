package mvc.aop.annotation;

import java.lang.annotation.*;

/**
 * Aspect
 *
 * @author cc
 * @description 这个注解是用于标记在''切面''中，即实现代理功能的类上面。
 * @since 2023/5/23 14:30
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleAspect {
    Class<? extends Annotation> target();
}
