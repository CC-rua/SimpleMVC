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
        //获取所有代理节点对象
        List<AdviceChainNode> adviceChainNodes = beanContainer.getBeanByAnnotation(SimpleAspect.class)
                .stream()
                .map(this::getAdviceChainNode)
                .toList();
        //增强所有需要被增强的对象
        beanContainer.getAllKey().stream()
                //被增强的对象不能又是一个切面
                .filter(bean -> !bean.isAnnotationPresent(SimpleAspect.class))
                .forEach(bean -> {
                    List<AdviceChainNode> matchBeanNode = getMatchBeanNode(adviceChainNodes, bean);
                    if (matchBeanNode.isEmpty()) {
                        return;
                    }
                    aop(matchBeanNode, bean);
                });
    }


    /**
     * 获取所有匹配的beanNode节点
     *
     * @param adviceChainNodes 全部beanNode节点
     * @param bean             过滤条件
     * @return List<AdviceChainNode>
     */
    private List<AdviceChainNode> getMatchBeanNode(List<AdviceChainNode> adviceChainNodes, Class<?> bean) {
        return adviceChainNodes.stream().filter(node -> node.getProxyPointcut().matches(bean)).toList();
    }

    /**
     * 通过beanClass 创建代理链节点对象
     *
     * @param beanClass beanClass
     * @return AdviceChainNode
     */
    private AdviceChainNode getAdviceChainNode(Class<?> beanClass) {
        SimpleAspect simpleAspect = beanClass.getAnnotation(SimpleAspect.class);
        Object bean = beanContainer.getBean(beanClass);
        if (bean instanceof SimpleAdviceBase advice) {
            //创建切点解析
            ProxyPointcut proxyPointcut = new ProxyPointcut();
            proxyPointcut.setExpression(simpleAspect.pointcut());

            return new AdviceChainNode(advice, proxyPointcut, simpleAspect.order());
        }
        return null;
    }

    /**
     * 根据切面定义对bean对象生产Aop代理
     *
     * @param matchBeanNode 代理节点列表
     * @param bean          被代理对象
     */
    private void aop(List<AdviceChainNode> matchBeanNode, Class<?> bean) {
        ProxyAdvisor advisor = new ProxyAdvisor();
        matchBeanNode.forEach(advisor::addAdviceChainNode);
        try {
            Object proxy = ProxyCreator.createProxy(bean, advisor);
            beanContainer.addBean(bean, proxy);
        } catch (NoSuchMethodException | InvocationTargetException
                 | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
