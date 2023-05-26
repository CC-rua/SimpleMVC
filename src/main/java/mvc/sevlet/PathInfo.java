package mvc.sevlet;

import mvc.sevlet.annotation.RequestMethod;

/**
 * PathInfo
 *
 * @author cc
 * @description
 * @since 2023/5/26 17:43
 */
public class PathInfo {
    /**
     * 请求路径
     */
    private String path;
    /**
     * 请求方式
     */
    private RequestMethod method;

    public PathInfo(String path, RequestMethod method) {
        this.path = path;
        this.method = method;
    }
}
