package mvc.aop;

import mvc.aop.advice.SimpleAfterReturnAdvice;
import mvc.aop.advice.SimpleThrowAdvice;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.Callable;

/**
 * ProxyAdvisor
 *
 * @author cc
 * @description 代理对象方法执行时，执行环绕通知
 * @since 2023/5/23 15:11
 */
public class ProxyAdvisor {

    private ArrayList<AdviceChainNode> adviceChainNodes;

    public ProxyAdvisor() {
        this.adviceChainNodes = new ArrayList<>();
    }

    private void orderChainList() {
        adviceChainNodes.sort(Comparator.comparingInt(AdviceChainNode::getOrder));
    }

    public void addAdviceChainNode(AdviceChainNode chainNode) {
        adviceChainNodes.add(chainNode);
        orderChainList();
    }

    /**
     * 多个切面的执行顺序是一种'先入后出'的顺序。比如说有两个切面Aspect1和Aspect2，
     * 那么他们的执行顺序应该是 Aspect1@before()->Aspect2@before()->targetClass@method()->Aspect2@after()->Aspect1@after()，
     * 先执行的 Aspect1@before() 方法要在最后执行 Aspect1@after()。
     *
     * @param beProxyObj 被代理对象
     * @param args       方法参数
     * @param callable   实际应该执行方法
     * @param method     被代理方法
     * @return 原方法的执行结果
     */
    @RuntimeType
    public Object intercept(@This Object beProxyObj,
                            @AllArguments Object[] args,
                            @SuperCall Callable<?> callable,
                            @Origin Method method) {
        //执行前处理
        for (AdviceChainNode adviceChainNode : adviceChainNodes) {
            adviceChainNode.doBefore(beProxyObj, method, args);
        }
        Object result = null;
        try {
            //实际执行方法
            result = callable.call();
        } catch (Exception e) {
            //异常处理
            for (AdviceChainNode adviceChainNode : adviceChainNodes) {
                adviceChainNode.doAfterThrow(beProxyObj, method, args);
            }
        }
        //后置处理
        for (int i = adviceChainNodes.size() - 1; i >= 0; i--) {
            AdviceChainNode adviceChainNode = adviceChainNodes.get(i);
            adviceChainNode.doAfter(beProxyObj, method, args);
        }
        return result;
    }
}
