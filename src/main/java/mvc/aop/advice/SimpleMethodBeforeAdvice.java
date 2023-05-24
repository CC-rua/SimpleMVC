package mvc.aop.advice;

import java.lang.reflect.Method;

/**
 * simpleBeforeAdvice
 *
 * @author cc
 * @description 继承这个通知接口并实现其前置方法，可以前置增强目标类，即目标方法执行前会先执行这个前置方法。
 * @since 2023/5/23 14:26
 */
public interface SimpleMethodBeforeAdvice extends SimpleAdviceBase {
    void before(Class<?> targetClass, Method method, Object[] args);
}
