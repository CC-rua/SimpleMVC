package mvc.util.test;

import lombok.extern.slf4j.Slf4j;
import mvc.aop.advice.SimpleAroundAdvice;
import mvc.aop.annotation.SimpleAspect;
import mvc.container.annotation.SimpleComponent;

import java.lang.reflect.Method;

/**
 * Aspect
 *
 * @author cc
 * @description
 * @since 2023/5/23 16:06
 */
@Slf4j
@SimpleComponent
@SimpleAspect(pointcut = "execution(* mvc.util.test..*.sayHello(..))", order = 1)
public class Aspect implements SimpleAroundAdvice {
    @Override
    public void after(Object beProxyObj, Method method, Object[] args) {
        log.info("After  DoodleAspect ----> class: {}, method: {}", beProxyObj.getClass().getName(), method.getName());
    }

    @Override
    public void before(Object beProxyObj, Method method, Object[] args) {
        log.info("Before  DoodleAspect ----> class: {}, method: {}", beProxyObj.getClass().getName(), method.getName());
    }

    @Override
    public void afterThrow(Object beProxyObj, Method method, Object[] args) {
        log.info("afterThrow  DoodleAspect ----> class: {}, method: {}", beProxyObj.getClass().getName(), method.getName());
    }
}
