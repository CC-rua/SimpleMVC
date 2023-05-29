package mvc.sevlet;

import lombok.extern.slf4j.Slf4j;
import mvc.sevlet.annotation.RequestMethod;
import mvc.util.InstanceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DespatcherSevlet
 *
 * @author cc
 * @description
 * @since 2023/5/29 10:42
 */
@Slf4j
public class DispatcherSevlet extends HttpServlet {

    private final ControllerDespatcher controllerDespatcher;

    private final ResultRender resultRender;

    public DispatcherSevlet() {
        this.controllerDespatcher = InstanceFactory.getInstance(ControllerDespatcher.class);
        this.resultRender = InstanceFactory.getInstance(ResultRender.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        //获取controller信息
        String path = req.getPathInfo();
        RequestMethod method = RequestMethod.valueOf(req.getMethod());
        ControllerInfo controllerInfo = controllerDespatcher.getControllerInfo(path, method);
        if (controllerInfo == null) {
            log.error("can not get any controllerInfo path:{},method:{}", path, method);
            return;
        }
        resultRender.invokeController(req, resp, controllerInfo);
    }
}
