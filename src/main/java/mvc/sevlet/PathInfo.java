package mvc.sevlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import mvc.sevlet.annotation.RequestMethod;

/**
 * PathInfo
 *
 * @author cc
 * @description
 * @since 2023/5/26 17:43
 */
@Data
@AllArgsConstructor
public class PathInfo {
    /**
     * 请求路径
     */
    private String path;
    /**
     * 请求方式
     */
    private RequestMethod method;
}
