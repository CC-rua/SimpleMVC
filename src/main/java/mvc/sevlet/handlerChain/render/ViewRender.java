package mvc.sevlet.handlerChain.render;

import mvc.SimpleMvc;
import mvc.sevlet.SimpleModelAndView;
import mvc.sevlet.handlerChain.ControllerContext;
import mvc.sevlet.handlerChain.ControllerRender;
import mvc.util.customException.ResultRenderResolverException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewRender implements ControllerRender {
    private final SimpleModelAndView result;

    public ViewRender(Object result) {
        if (result instanceof SimpleModelAndView) {
            this.result = (SimpleModelAndView) result;
        } else if (result instanceof String) {
            this.result = new SimpleModelAndView().setView((String) result);
        } else {
            throw new RuntimeException("返回类型不合法");
        }
    }

    @Override
    public void render(ControllerContext context) {
        String path;
        path = result.getView();
        HttpServletRequest req = context.getReq();
        HttpServletResponse resp = context.getResp();
        result.getModel().forEach(req::setAttribute);
        //以moduleAndView格式返回，需要重定向资源
        try {
            req.getRequestDispatcher(SimpleMvc.getConfiguration().getResourcePath() + path).forward(req, resp);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
