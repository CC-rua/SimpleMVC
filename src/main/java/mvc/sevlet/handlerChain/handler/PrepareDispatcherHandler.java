package mvc.sevlet.handlerChain.handler;

import lombok.extern.slf4j.Slf4j;
import mvc.sevlet.handlerChain.ControllerContext;
import mvc.sevlet.handlerChain.ControllerHandler;

@Slf4j
public class PrepareDispatcherHandler implements ControllerHandler {
    @Override
    public boolean handle(ControllerContext context) throws Exception {
        //设置编码
        context.getReq().setCharacterEncoding("UTF-8");
        String requestPath = context.getRequestPath();
        if (requestPath.length() > 1 && requestPath.endsWith("/")) {
            context.setRequestPath(requestPath.substring(0, requestPath.length() - 1));
        }
        log.info("[SimpleMVC] {} {}", context.getRequestMethod(), context.getRequestPath());
        return true;
    }
}
