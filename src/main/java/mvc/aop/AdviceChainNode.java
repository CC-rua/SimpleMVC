package mvc.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mvc.aop.advice.SimpleAdviceBase;
import mvc.aop.advice.SimpleAfterReturnAdvice;
import mvc.aop.advice.SimpleMethodBeforeAdvice;
import mvc.aop.advice.SimpleThrowAdvice;

import java.lang.reflect.Method;

/**
 * AdviceChain
 *
 * @author cc
 * @description 通知链
 * @since 2023/5/26 10:23
 */
@Getter
@Setter
@AllArgsConstructor
public class AdviceChainNode {
    /**
     * 切面方法
     */
    private SimpleAdviceBase advice;
    /**
     * 切点
     */
    private ProxyPointcut proxyPointcut;
    /**
     * 切面执行排序
     */
    private int order;

    public void doBefore(Object beProxyObj, Method method, Object[] args) {
        if (!proxyPointcut.matches(method)) {
            return;
        }
        if (advice instanceof SimpleMethodBeforeAdvice beforeAdvice) {
            beforeAdvice.before(beProxyObj, method, args);
        }
    }

    public void doAfterThrow(Object beProxyObj, Method method, Object[] args) {
        if (!proxyPointcut.matches(method)) {
            return;
        }
        if (advice instanceof SimpleThrowAdvice beforeAdvice) {
            beforeAdvice.afterThrow(beProxyObj, method, args);
        }
    }

    public void doAfter(Object beProxyObj, Method method, Object[] args) {
        if (!proxyPointcut.matches(method)) {
            return;
        }
        if (advice instanceof SimpleAfterReturnAdvice beforeAdvice) {
            beforeAdvice.after(beProxyObj, method, args);
        }
    }
}
