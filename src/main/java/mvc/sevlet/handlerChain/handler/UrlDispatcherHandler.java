package mvc.sevlet.handlerChain.handler;

import lombok.extern.slf4j.Slf4j;
import mvc.SimpleMvc;
import mvc.sevlet.handlerChain.ControllerContext;
import mvc.sevlet.handlerChain.ControllerHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 * 用于处理静态资源，当碰到资源是静态资源时就直接转发请求到 Tomcat 默认的 servlet 去。
 */
@Slf4j
public class UrlDispatcherHandler implements ControllerHandler {

    /**
     * tomcat 默认 RequestDispatcher 的名称
     * TODO: 其他服务器默认的 RequestDispatcher. 如 WebLogic 为 FileServlet
     */
    private static final String TOMCAT_DEFAULT_SERVLET = "default";

    private final ServletContext servletContext;
    /**
     * 默认的 RequestDispatcher, 处理静态资源
     */
    private RequestDispatcher defaultServlet;

    public UrlDispatcherHandler(ServletContext servletContext) {

        this.servletContext = servletContext;
        defaultServlet = servletContext.getNamedDispatcher(TOMCAT_DEFAULT_SERVLET);

        if (null == defaultServlet) {
            throw new RuntimeException("没有默认的 Servlet");
        }

        log.info("The default servlet for serving static resource is [{}]", TOMCAT_DEFAULT_SERVLET);
    }

    @Override
    public boolean handle(ControllerContext context) throws Exception {
        if (isStaticResource(context.getRequestPath())) {
            defaultServlet.forward(context.getReq(), context.getResp());
            return false;
        }
        return true;
    }
    /**
     * 是否为静态资源
     */
    private boolean isStaticResource(String url) {
        return url.startsWith(SimpleMvc.getConfiguration().getAssetPath());
    }
}
