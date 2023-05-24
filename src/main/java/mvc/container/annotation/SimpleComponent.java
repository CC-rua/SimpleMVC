package mvc.container.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SimpleComponent
 *
 * @description 标记是一个 bean
 * @author cc
 * @since 2023/5/22 16:10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleComponent {
}
