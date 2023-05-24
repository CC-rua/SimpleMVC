package mvc.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * ClassUtil
 *
 * @author cc
 * @description 关于反射使用 class 的工具类
 * @since 2023/5/22 16:22
 */
public class ClassUtil {

    private ClassUtil() {
        throw new IllegalArgumentException("Util Class");
    }

    /**
     * 获取 classLoader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取包路径下所有 class 文件
     *
     * @param basePackage 包路径
     * @return List<? extends Class < ?>>
     */
    public static List<? extends Class<?>> getClassesByPackage(String basePackage) {
        URL url = getClassLoader().getResource(basePackage.replace(".", "/"));

        if (url == null) {
            return new ArrayList<>();
        }
        if (url.getProtocol().equals(Constant.FILE_PROTOCOL)) {
            //是一个普通文件
            File file = new File(url.getFile());
            Path basePath = file.toPath();
            //遍历所有.class文件获取 class
            try (Stream<Path> walk = Files.walk(file.toPath())) {
                return walk.filter(path -> path.toFile().getName().endsWith(".class"))
                        .map(path -> getClassByPath(path, basePath, basePackage))
                        .filter(Objects::nonNull)
                        .toList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (url.getProtocol().equals(Constant.JAR_PROTOCOL)) {

        }
        return new ArrayList<>();
    }

    /**
     * 通过类包路径，加载类 返回值可能为空
     *
     * @param path        全路径
     * @param basePath    基础路径
     * @param basePackage 包名
     * @return Class<?>
     */
    private static Class<?> getClassByPath(Path path, Path basePath, String basePackage) {
        String packageName = path.toString().replace(basePath.toString(), "");
        String className = (basePackage + packageName)
                .replace("\\", ".")
                .replace("/", ".")
                .replace(".class", "");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载一个对象
     *
     * @param cla 对象的 Class 对象
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T instanceClass(Class<T> cla) {
        try {
            Constructor<?> constructor = cla.getConstructor();
            return (T) constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置一个对象的属性
     *
     * @param o             对象
     * @param field         对象属性
     * @param classInstance 目标值
     */
    public static void setField(Object o, Field field, Object classInstance) throws IllegalAccessException {
        field.set(o, classInstance);
    }
}
