package mvc.sevlet.handlerChain;

import lombok.Getter;
import lombok.Setter;
import mvc.sevlet.annotation.RequestMethod;
import mvc.sevlet.handlerChain.render.InternalErrorRender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * controller 处理上下文
 */
public class ControllerContext {
    /**
     * 请求体
     */
    @Getter
    private final HttpServletRequest req;
    /**
     * 请求回复
     */
    @Getter
    private final HttpServletResponse resp;
    /**
     * handler 遍历器
     */
    private final Iterator<ControllerHandler> iterator;
    /**
     * 请求方式
     */
    @Getter
    private final RequestMethod requestMethod;

    /**
     * 控制器处理器
     */
    @Getter
    @Setter
    private ControllerRender render;

    /**
     * 请求路径
     */
    @Getter
    @Setter
    private String requestPath;
    /**
     * 请求状态码
     */
    @Getter
    private final int responseStatus;

    public ControllerContext(HttpServletRequest req, HttpServletResponse resp, Iterator<ControllerHandler> iterator) {

        this.req = req;
        this.resp = resp;
        this.iterator = iterator;
        this.requestPath = req.getPathInfo();
        this.responseStatus = HttpServletResponse.SC_OK;
        this.requestMethod = RequestMethod.valueOf(req.getMethod());
    }

    /**
     * 执行责任链
     */
    public void doHandlerChain() {
        try {

            while (iterator.hasNext()) {
                ControllerHandler next = iterator.next();
                if (next == null) {
                    continue;
                }
                next.handle(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            render = new InternalErrorRender();
        }
    }

    /**
     * 执行结果处理器
     */
    public void doRender() {
        try {
            if (render == null) {
                return;
            }
            render.render(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
