package mvc.ioc;

import mvc.container.SimpleBeanContainer;
import mvc.ioc.annotation.IocException;
import mvc.ioc.annotation.SimpleAutowired;
import mvc.util.ClassUtil;
import mvc.util.InstanceFactory;

import java.util.Arrays;
import java.util.List;

/**
 * SimpleIoc
 *
 * @author cc
 * @description 简易IOC装配
 * @since 2023/5/22 16:07
 */
public class SimpleIoc {
    /**
     * bean 容器
     */
    private final SimpleBeanContainer beanContainer;

    public SimpleIoc() {
        beanContainer = InstanceFactory.getInstance(SimpleBeanContainer.class);
    }

    /**
     * 对所有bean执行ioc()
     */
    public void doIco() {
        beanContainer.getAllBean().forEach(this::ioc);
    }

    /**
     * 给bean初始化值
     */
    public void ioc(Object o) {
        Arrays.stream(o.getClass().getFields())
                .filter(field -> field.isAnnotationPresent(SimpleAutowired.class))
                .forEach(field -> {
                    try {
                        Object classInstance = getClassInstance(field.getType());
                        ClassUtil.setField(o, field, classInstance);
                    } catch (IocException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 从容器中获取一个类的实例对象
     *
     * @param _fieldClass 指定类
     * @return Object
     * @throws IocException IOC执行异常
     */
    private Object getClassInstance(Class<?> _fieldClass) throws IocException {
        if (_fieldClass.isInterface()) {
            //这个 field 是一个接口，尝试获取接口的实现类
            List<Class<?>> beanBySuper = beanContainer.getBeanBySuper(_fieldClass);
            if (beanBySuper.isEmpty()) {
                throw new IocException("not found bean" + _fieldClass.getName());
            }
            if (beanBySuper.size() > 1) {
                throw new IocException("bean not define" + _fieldClass.getName());
            }
            return beanBySuper.stream().findFirst();
        } else {
            Object bean = beanContainer.getBean(_fieldClass);
            if (bean == null) {
                //查不到指定的bean
                throw new IocException("not found bean" + _fieldClass.getName());
            }
            return bean;
        }
    }
}
