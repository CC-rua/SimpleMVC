package mvc.sevlet.handlerChain.render;

import com.alibaba.fastjson.JSON;
import mvc.sevlet.handlerChain.ControllerContext;
import mvc.sevlet.handlerChain.ControllerRender;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonRender implements ControllerRender {
    private final Object result;

    public JsonRender(Object result) {
        this.result = result;
    }

    @Override
    public void render(ControllerContext context) {
        HttpServletResponse resp = context.getResp();
        //以json格式返回结果
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(JSON.toJSONString(result));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
