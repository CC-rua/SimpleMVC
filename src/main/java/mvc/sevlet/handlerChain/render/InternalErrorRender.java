package mvc.sevlet.handlerChain.render;

import mvc.sevlet.handlerChain.ControllerContext;
import mvc.sevlet.handlerChain.ControllerRender;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InternalErrorRender implements ControllerRender {
    @Override
    public void render(ControllerContext context) {
        try {
            context.getResp().sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
