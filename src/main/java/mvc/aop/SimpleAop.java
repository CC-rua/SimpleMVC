package mvc.aop;

import mvc.aop.advice.SimpleAdviceBase;
import mvc.aop.annotation.SimpleAspect;
import mvc.container.SimpleBeanContainer;
import mvc.util.InstanceFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * SimpleAop
 *
 * @author cc
 * @description 切面简单实现
 * @since 2023/5/23 14:34
 */
public class SimpleAop {
    private final SimpleBeanContainer beanContainer;

    public SimpleAop() {
        beanContainer = InstanceFactory.getInstance(SimpleBeanContainer.class);
    }

    /**
     * 对所有bean执行aop()
     */
    public void doAop() {
        beanContainer.getAllBean().stream()
                .filter(SimpleAdviceBase.class::isInstance)
                .forEach(bean -> {
                    SimpleAdviceBase adviceBase = (SimpleAdviceBase) bean;
                    aop(adviceBase);
                });
    }

    /**
     * 根据切面定义对bean对象生产Aop代理
     *
     * @param advice 切面对象
     */
    public void aop(SimpleAdviceBase advice) {
        Class<?> oClass = advice.getClass();
        if (oClass.isAnnotationPresent(SimpleAspect.class)) {
            SimpleAspect annotation = oClass.getAnnotation(SimpleAspect.class);
            //获取切点匹配器
            ProxyPointcut proxyPointcut = new ProxyPointcut();
            proxyPointcut.setExpression(annotation.pointcut());
            //创建通知处理对象
            ProxyAdvisor advisor = new ProxyAdvisor(advice, proxyPointcut);
            beanContainer.getAllKey().stream()
                    //被增强的对象不能又是一个切面
                    .filter(bean -> !bean.isAnnotationPresent(SimpleAspect.class))
                    .filter(proxyPointcut::matches)
                    .forEach(bean -> {
                        //创建代理对象
                        Object proxyBean = null;
                        try {
                            proxyBean = ProxyCreator.createProxy(bean, advisor);
                        } catch (NoSuchMethodException | InvocationTargetException
                                | InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        //注册到bean容器
                        beanContainer.addBean(bean, proxyBean);
                    });
        }
    }
}
