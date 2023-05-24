package mvc.aop;

import mvc.aop.advice.SimpleAdviceBase;
import mvc.aop.advice.SimpleAfterReturnAdvice;
import mvc.aop.advice.SimpleMethodBeforeAdvice;
import mvc.aop.advice.SimpleThrowAdvice;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * ProxyAdvisor
 *
 * @author cc
 * @description 代理对象方法执行时，执行环绕通知
 * @since 2023/5/23 15:11
 */
public record ProxyAdvisor(SimpleAdviceBase advice) {

    public Object doProxy(Object target, Class<?> targetClass, Method method, Object[] args, MethodProxy proxy) {
        Object result = null;
        if (advice instanceof SimpleMethodBeforeAdvice beforeAdvice) {
            beforeAdvice.before(targetClass, method, args);
        }
        try {
            //实际执行方法
            result = proxy.invokeSuper(target, args);
            if (advice instanceof SimpleAfterReturnAdvice returnAdvice) {
                returnAdvice.after(targetClass, method, args);
            }
        } catch (Throwable e) {
            if (advice instanceof SimpleThrowAdvice throwAdvice) {
                throwAdvice.afterThrow(targetClass, method, args);
            }
        }
        return result;
    }
}
