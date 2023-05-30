package mvc.sevlet;

import mvc.container.SimpleBeanContainer;
import mvc.sevlet.annotation.RequestMapping;
import mvc.sevlet.annotation.RequestMethod;
import mvc.sevlet.annotation.RequestParam;
import mvc.util.InstanceFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * DispatcherServlet
 *
 * @author cc
 * @description
 * @since 2023/5/26 16:14
 */
public class ControllerDispatcher {

    private final Map<PathInfo, ControllerInfo> handlerDespatcherMap;

    private final SimpleBeanContainer beanContainer;

    public ControllerDispatcher() {
        this.handlerDespatcherMap = new HashMap<>();
        beanContainer = InstanceFactory.getInstance(SimpleBeanContainer.class);
    }

    /**
     * 初始化
     */
    public void doInit() {
        beanContainer.getAllKey().stream()
                .filter(cla -> cla.isAnnotationPresent(RequestMapping.class))
                .forEach(this::putPathController);
    }

    public void putPathController(Class<?> cla) {
        RequestMapping baseMapping = cla.getAnnotation(RequestMapping.class);

        Method[] methods = cla.getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
            String path = baseMapping.value() + methodMapping.value();

            PathInfo pathInfo = new PathInfo(path, methodMapping.method());

            ControllerInfo controllerInfo = new ControllerInfo(cla, method);

            for (Parameter typeParameter : method.getParameters()) {
                RequestParam annotation = typeParameter.getAnnotation(RequestParam.class);
                controllerInfo.addParam(annotation.value(), typeParameter.getType());
            }

            handlerDespatcherMap.put(pathInfo, controllerInfo);
        }
    }

    public ControllerInfo getControllerInfo(String path, RequestMethod method) {
        PathInfo pathInfo = new PathInfo(path, method);
        return handlerDespatcherMap.get(pathInfo);
    }

}
