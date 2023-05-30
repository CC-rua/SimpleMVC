package mvc.sevlet.handlerChain.handler;

import lombok.extern.slf4j.Slf4j;
import mvc.container.SimpleBeanContainer;
import mvc.sevlet.ControllerDispatcher;
import mvc.sevlet.ControllerInfo;
import mvc.sevlet.annotation.ResponseBody;
import mvc.sevlet.annotation.RequestMethod;
import mvc.sevlet.handlerChain.ControllerContext;
import mvc.sevlet.handlerChain.ControllerHandler;
import mvc.sevlet.handlerChain.ControllerRender;
import mvc.sevlet.handlerChain.render.JsonRender;
import mvc.sevlet.handlerChain.render.ViewRender;
import mvc.util.CastUtil;
import mvc.util.InstanceFactory;
import mvc.util.ValidateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SimpleControllerDispatcherHandler implements ControllerHandler {
    private SimpleBeanContainer beanContainer;
    private ControllerDispatcher controllerDispatcher;

    public SimpleControllerDispatcherHandler() {
        beanContainer = InstanceFactory.getInstance(SimpleBeanContainer.class);
        controllerDispatcher = InstanceFactory.getInstance(ControllerDispatcher.class);
    }

    @Override
    public boolean handle(ControllerContext context) throws Exception {
        String path = context.getRequestPath();

        RequestMethod method = context.getRequestMethod();

        ControllerInfo controllerInfo = controllerDispatcher.getControllerInfo(path, method);

        if (controllerInfo == null) {
            log.error("can not found controller path:{} method:{}",path,method);
            return false;
        }
        Object result = invokeController(context.getReq(), context.getResp(), controllerInfo);
        setRender(result, controllerInfo, context);
        return false;
    }

    public Object invokeController(HttpServletRequest req, HttpServletResponse resp, ControllerInfo controllerInfo) {
        Object bean = beanContainer.getBean(controllerInfo.getCla());
        if (bean == null) {
            log.error("resultRender invokeController bean not found controllerInfo:{}", controllerInfo);
            return bean;
        }
        Object result = null;
        try {
            if (controllerInfo.getParams().size() == 0) {
                result = controllerInfo.getMethod().invoke(bean);
            } else {
                Object[] args = initInvokeParams(req, controllerInfo.getParams());
                result = controllerInfo.getMethod().invoke(bean, args);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 初始化参数
     *
     * @param req                  请求体
     * @param controllerInfoParams controllerInfo中保留的param类型
     * @return Object[]
     */
    private Object[] initInvokeParams(HttpServletRequest req, Map<String, Class<?>> controllerInfoParams) {
        Map<String, String> paramsMap = getParamsMap(req);
        return paramsMap.keySet().stream().map(key -> {
            String value = paramsMap.get(key);
            Class<?> type = controllerInfoParams.get(key);
            Object v = null;
            if (value == null) {
                v = CastUtil.primitiveNull(type);
            } else {
                v = CastUtil.convert(type, value);
                //TODO： 非源生类型转换
            }
            return v;
        }).toList().toArray();
    }

    /**
     * 获取请求体里的所有params
     *
     * @param req 请求体
     * @return Map<String, String>
     */
    private Map<String, String> getParamsMap(HttpServletRequest req) {
        Map<String, String> paramsMap = new HashMap<>();
        RequestMethod method = RequestMethod.valueOf(req.getMethod());
        switch (method) {
            case GET, POST -> req.getParameterMap().forEach((key, value) -> {
                //GET、POST 请求方式获取请求参数
                if (ValidateUtil.isNotEmpty(value)) {
                    paramsMap.put(key, value[0]);
                }
            });
            default -> log.error("can not getParams method:{} not found ", method);
        }

        return paramsMap;
    }

    /**
     * 设置请求结果执行器
     */
    private void setRender(Object result, ControllerInfo controllerInfo, ControllerContext handlerChain) {
        if (null == result) {
            return;
        }
        ControllerRender render;
        boolean isJson = controllerInfo.getMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson) {
            render = new JsonRender(result);
        } else {
            render = new ViewRender(result);
        }
        handlerChain.setRender(render);
    }
}
