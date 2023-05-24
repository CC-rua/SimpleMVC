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
@SimpleAspect(target = SimpleComponent.class)
public class Aspect implements SimpleAroundAdvice {
    @Override
    public void after(Class<?> targetClass, Method method, Object[] args) {
        log.info("After  DoodleAspect ----> class: {}, method: {}", targetClass.getName(), method.getName());
    }

    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) {
        log.info("Before  DoodleAspect ----> class: {}, method: {}", targetClass.getName(), method.getName());
    }

    @Override
    public void afterThrow(Class<?> targetClass, Method method, Object[] args) {
        log.info("afterThrow  DoodleAspect ----> class: {}, method: {}", targetClass.getName(), method.getName());
    }
}
