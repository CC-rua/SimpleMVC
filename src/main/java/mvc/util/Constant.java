package mvc.util;

import mvc.container.annotation.SimpleComponent;
import mvc.container.annotation.SimpleController;
import mvc.container.annotation.SimpleRepository;
import mvc.container.annotation.SimpleService;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * Constant
 *
 * @author cc
 * @description 常量
 * @since 2023/5/22 16:14
 */
public class Constant {
    private Constant() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * file 形式 url 协议
     */
    public static final String FILE_PROTOCOL = "file";
    /**
     * jar 形式 url 协议
     */
    public static final String JAR_PROTOCOL = "jar";
    /**
     * 标识 bean 的注解
     */
    public static final List<Class<? extends Annotation>> BEAN_ANNOTATION
            = Arrays.asList(SimpleComponent.class, SimpleController.class, SimpleRepository.class, SimpleService.class);
}
