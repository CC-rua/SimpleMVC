package mvc.util;

/**
 * ValidateUtil
 *
 * @author cc
 * @description
 * @since 2023/5/29 11:02
 */
public class ValidateUtil {

    public static boolean isNotEmpty(String[] value) {
        if (value == null) {
            return false;
        }
        if (value.length <= 0) {
            return false;
        }
        return true;
    }
    /**
     * String是否为null或""
     *
     * @param obj String
     * @return 是否为空
     */
    public static boolean isEmpty(String obj) {
        return (obj == null || "".equals(obj));
    }
}
