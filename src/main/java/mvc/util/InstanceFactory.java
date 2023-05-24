package mvc.util;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InstanceFactory
 *
 * @author cc
 * @description
 * @since 2023/5/23 14:38
 */
public class InstanceFactory {
    private static final Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<>();

    private InstanceFactory() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> cla) {
        synchronized (InstanceFactory.class) {
            Object o = instanceMap.get(cla);
            if (o == null) {
                try {
                    Constructor<T> constructor = cla.getConstructor();
                    synchronized (InstanceFactory.class) {
                        o = instanceMap.get(cla);
                        if (o == null) {
                            o = constructor.newInstance();
                            instanceMap.put(cla, o);
                        } else {
                            return (T) o;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return (T) o;
        }
    }
}
