package mvc.sevlet;

import lombok.Data;

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
@Data
public class ControllerInfo {

    private Class<?> cla;

    private final Map<String, Class<?>> params;

    private Method method;


    public ControllerInfo() {
        this.params = new HashMap<>();
    }

    public ControllerInfo(Class<?> cla, Method method) {
        this();
        this.cla = cla;
        this.method = method;
    }

    public void addParam(String value, Class<?> type) {
        params.put(value, type);
    }

}
