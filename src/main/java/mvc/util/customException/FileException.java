package mvc.util.customException;

/**
 * FileException
 *
 * @author cc
 * @description
 * @since 2023/5/30 9:47
 */
public class FileException extends RuntimeException {
    public FileException(String message) {
        super(message);
    }
}
