package mvc.aop;

import mvc.aop.advice.SimpleAdviceBase;
import mvc.aop.advice.SimpleAfterReturnAdvice;
import mvc.aop.advice.SimpleMethodBeforeAdvice;
import mvc.aop.advice.SimpleThrowAdvice;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * ProxyAdvisor
 *
 * @author cc
 * @description 代理对象方法执行时，执行环绕通知
 * @since 2023/5/23 15:11
 */
public record ProxyAdvisor(SimpleAdviceBase advice, ProxyPointcut pointcut) {

    @RuntimeType
    public Object intercept(@This Object beProxyObj,
                            @AllArguments Object[] args,
                            @SuperCall Callable<?> callable,
                            @Origin Method method) {

        //不满足切点需要代理的方法
        if (!pointcut.matches(method)) {
            try {
                return callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Object result = null;
        if (advice instanceof SimpleMethodBeforeAdvice beforeAdvice) {
            beforeAdvice.before(beProxyObj, method, args);
        }
        try {
            //实际执行方法
            result = callable.call();
            if (advice instanceof SimpleAfterReturnAdvice returnAdvice) {
                returnAdvice.after(beProxyObj, method, args);
            }
        } catch (Exception e) {
            if (advice instanceof SimpleThrowAdvice throwAdvice) {
                throwAdvice.afterThrow(beProxyObj, method, args);
            }
        }
        return result;
    }
}
