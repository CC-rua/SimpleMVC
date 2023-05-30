package mvc.sevlet;

import lombok.extern.slf4j.Slf4j;
import mvc.sevlet.handlerChain.ControllerContext;
import mvc.sevlet.handlerChain.ControllerHandler;
import mvc.sevlet.handlerChain.handler.PrepareDispatcherHandler;
import mvc.sevlet.handlerChain.handler.SimpleControllerDispatcherHandler;
import mvc.sevlet.handlerChain.handler.UrlDispatcherHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * DespatcherSevlet
 *
 * @author cc
 * @description
 * @since 2023/5/29 10:42
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    private List<ControllerHandler> handlerChain;

    public DispatcherServlet() {

    }

    @Override
    public void init() throws ServletException {
        super.init();
        handlerChain = new ArrayList<>();
        handlerChain.add(new PrepareDispatcherHandler());
        handlerChain.add(new UrlDispatcherHandler(getServletContext()));
        handlerChain.add(new SimpleControllerDispatcherHandler());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        ControllerContext context = new ControllerContext(req, resp, handlerChain.iterator());
        context.doHandlerChain();
        context.doRender();
    }
}
