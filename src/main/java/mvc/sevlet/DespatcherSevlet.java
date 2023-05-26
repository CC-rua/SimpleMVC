package mvc.sevlet;

import mvc.sevlet.annotation.RequestMapping;
import mvc.sevlet.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/**
 * DespatchSevlet
 *
 * @author cc
 * @description
 * @since 2023/5/26 16:14
 */
public class DespatcherSevlet {

    private Map<PathInfo, ControllerInfo> handlerDespatcherMap;


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
            ControllerInfo controllerInfo = new ControllerInfo();
            controllerInfo.setClass(cla);
            controllerInfo.setMethod(method);
            for (Parameter typeParameter : method.getParameters()) {
                RequestParam annotation = typeParameter.getAnnotation(RequestParam.class);
                controllerInfo.addParam(annotation.value(), typeParameter.getType());
            }

            handlerDespatcherMap.put(pathInfo, controllerInfo);

        }
    }

}
