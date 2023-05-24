package mvc.container;

import mvc.util.ClassUtil;
import mvc.util.Constant;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SimpleBeanContainer
 *
 * @author cc
 * @description 简易的 bean 容器 参考实现 https://zzzzbw.cn/article/9/
 * @since 2023/5/22 15:58
 */
public class SimpleBeanContainer {

    /**
     * 存放所有bean对象
     */
    private final Map<Class<?>, Object> beanContainer;

    public SimpleBeanContainer() {
        beanContainer = new ConcurrentHashMap<>();
    }

    public void addBean(Class<?> cla, Object bean) {
        beanContainer.put(cla, bean);
    }

    public void removeBean(Class<?> cla) {
        beanContainer.remove(cla);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> cla) {
        return (T) beanContainer.get(cla);
    }

    public List<Object> getAllBean() {
        return new ArrayList<>(beanContainer.values());
    }

    public List<Class<?>> getAllKey() {
        return new ArrayList<>(beanContainer.keySet());
    }

    /**
     * 加载指定路径下所有实现了标识一个 bean 注解的所有类
     *
     * @param beanPath 基础路径
     */
    public void loadBean(String beanPath) {
        ClassUtil.getClassesByPackage(beanPath).stream()
                .filter(cla -> {
                    for (Class<? extends Annotation> ann : Constant.BEAN_ANNOTATION) {
                        if (cla.isAnnotationPresent(ann)) {
                            return true;
                        }
                    }
                    return false;
                })
                .forEach(clz -> addBean(clz, ClassUtil.instanceClass(clz)));
    }

    /**
     * 获取指定超类的所有子类的class类型
     *
     * @param superClass 指定超类
     * @return List<Class < ?>>
     */
    public List<Class<?>> getBeanBySuper(Class<?> superClass) {
        return getAllKey().stream()
                .filter(superClass::isAssignableFrom)
                .toList();
    }

    /**
     * 获取指定注解的所有子类的class类型
     *
     * @param annotation 指定注解
     * @return List<Class < ?>>
     */
    public List<Class<?>> getBeanByAnnotation(Class<? extends Annotation> annotation) {
        return getAllKey().stream()
                .filter(cla -> cla.isAnnotationPresent(annotation))
                .toList();
    }
}
