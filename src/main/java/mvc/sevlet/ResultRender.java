package mvc.sevlet;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mvc.SimpleMvc;
import mvc.container.SimpleBeanContainer;
import mvc.sevlet.annotation.RequestBody;
import mvc.sevlet.annotation.RequestMethod;
import mvc.util.CastUtil;
import mvc.util.InstanceFactory;
import mvc.util.ValidateUtil;
import mvc.util.customException.ResultRenderResolverException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * ResultRender
 *
 * @author cc
 * @description servlet 结果处理器
 * @since 2023/5/29 10:44
 */
@Slf4j
public class ResultRender {
    private SimpleBeanContainer beanContainer;

    public ResultRender() {
        beanContainer = InstanceFactory.getInstance(SimpleBeanContainer.class);
    }

    public void invokeController(HttpServletRequest req, HttpServletResponse resp, ControllerInfo controllerInfo) {
        Object bean = beanContainer.getBean(controllerInfo.getCla());
        if (bean == null) {
            log.error("resultRender invokeController bean not found controllerInfo:{}", controllerInfo);
            return;
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

        try {
            resultResolver(controllerInfo, result, req, resp);
        } catch (ResultRenderResolverException | ServletException | IOException e) {
            e.printStackTrace();
            //统一处理异常，转发到 400 页面
        }
    }

    /**
     * 结果解析器
     *
     * @param controllerInfo 处理信息
     * @param result         处理结果
     * @param req            请求体
     * @param resp           响应体
     */
    private void resultResolver(ControllerInfo controllerInfo, Object result, HttpServletRequest req,
                                HttpServletResponse resp) throws ResultRenderResolverException, ServletException, IOException {
        if (result == null) {
            return;
        }
        boolean isJson = controllerInfo.getMethod().isAnnotationPresent(RequestBody.class);
        if (isJson) {
            //以json格式返回结果
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(JSON.toJSONString(result));
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String path;
            if (result instanceof String strResult) {
                path = strResult;
            } else if (result instanceof SimpleModelAndView mav) {
                path = mav.getView();
                mav.getModel().forEach(req::setAttribute);
            } else {
                throw new ResultRenderResolverException("result can not resolver result: " + result.getClass());
            }
            //以moduleAndView格式返回，需要重定向资源
            req.getRequestDispatcher(SimpleMvc.getConfiguration().getResourcePath() + path).forward(req, resp);
        }
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
}
