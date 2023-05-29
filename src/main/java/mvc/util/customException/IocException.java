package mvc.util.customException;

/**
 * IocException
 *
 * @author cc
 * @description
 * @since 2023/5/23 9:57
 */
public class IocException extends RuntimeException {
    public IocException(String message) {
        super(message);
    }
}
