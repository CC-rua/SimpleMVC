package mvc.sevlet.handlerChain;

/**
 * controller 处理器
 */
public interface ControllerHandler {
    boolean handle(ControllerContext context) throws Exception;
}
