package mvc.sevlet;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * ControllerInfo
 *
 * @author cc
 * @description
 * @since 2023/5/26 17:43
 */
public class ControllerInfo {

    private Class<?> cla;

    private Map<String, Class<?>> params;


    public ControllerInfo() {
        this.params = new HashMap<>();
    }

    public void addParam(String value, Class<?> type) {
        params.put(value, type);
    }

    public void setClass(Class<?> cla) {

    }

    public void setMethod(Method method) {

    }
}
