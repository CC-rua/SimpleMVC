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
    /**
    * @Description 切点匹配字符串
    * @Author zhujiaqi
    * @Date 2023/5/25 14:51
    **/
    String pointcut();

    /**
     * 切面方法执行的顺序
     * @return
     */
    int order();
}
