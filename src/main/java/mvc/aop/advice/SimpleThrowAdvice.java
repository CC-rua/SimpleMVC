package mvc.aop.advice;

import java.lang.reflect.Method;

/**
 * simpleBeforeAdvice
 *
 * @author cc
 * @description 继承这个通知接口并实现其异常方法，可以增强目标类的异常，即目标方法发生异常时，会执行这个异常方法。
 * @since 2023/5/23 14:26
 */
public interface SimpleThrowAdvice extends SimpleAdviceBase {
    void afterThrow(Class<?> targetClass, Method method, Object[] args);
}
