package mvc.sevlet.handlerChain.handler;

import lombok.extern.slf4j.Slf4j;
import mvc.SimpleMvc;
import mvc.sevlet.handlerChain.ControllerContext;
import mvc.sevlet.handlerChain.ControllerHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * 处理 jsp 页面的实现类JspHandler，当碰到资源是 jsp 页面时就直接转发请求到 Tomcat 的 jsp 的 servlet 去。
 */
@Slf4j
public class JspDispatcherHandler implements ControllerHandler {

    /**
     * tomcat 默认 RequestDispatcher 的名称
     * TODO: 其他服务器默认的 RequestDispatcher. 如 WebLogic 为 FileServlet
     */
    private static final String TOMCAT_JSP_SERVLET = "jsp";

    private final ServletContext servletContext;
    /**
     * 默认的 RequestDispatcher, 处理静态资源
     */
    private RequestDispatcher defaultServlet;

    public JspDispatcherHandler(ServletContext servletContext) {

        this.servletContext = servletContext;
        defaultServlet = servletContext.getNamedDispatcher(TOMCAT_JSP_SERVLET);

        if (null == defaultServlet) {
            throw new RuntimeException("没有 jsp Servlet");
        }

        log.info("The jsp servlet for serving static resource is [{}]", TOMCAT_JSP_SERVLET);
    }

    @Override
    public boolean handle(ControllerContext context) throws Exception {
        if (isPageView(context.getRequestPath())) {
            defaultServlet.forward(context.getReq(), context.getResp());
            return false;
        }
        return true;
    }
    /**
     * 是否为静态资源
     */
    private boolean isPageView(String url) {
        return url.startsWith(SimpleMvc.getConfiguration().getViewPath());
    }
}
