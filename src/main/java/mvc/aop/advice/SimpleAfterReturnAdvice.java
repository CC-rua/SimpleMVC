package mvc.aop.advice;

import java.lang.reflect.Method;

/**
 * simpleBeforeAdvice
 *
 * @author cc
 * @description 继承这个通知接口并实现其返回后方法，可以后置增强目标类，即目标方法执后并放回结果时，会执行这个返回方法。
 * @since 2023/5/23 14:26
 */
public interface SimpleAfterReturnAdvice extends SimpleAdviceBase {
    void after(Class<?> targetClass, Method method, Object[] args);
}
