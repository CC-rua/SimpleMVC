package mvc.sevlet;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mvc.SimpleMvc;
import mvc.container.SimpleBeanContainer;
import mvc.sevlet.annotation.ResponseBody;
import mvc.util.InstanceFactory;
import mvc.util.customException.ResultRenderResolverException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
        boolean isJson = controllerInfo.getMethod().isAnnotationPresent(ResponseBody.class);
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

        }
    }


}
