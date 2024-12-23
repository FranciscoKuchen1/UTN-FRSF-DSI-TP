package dsitp.backend.project.util;

public class NoUniqueIdException extends RuntimeException {

    /**
     * Creates a new instance of <code>NoUniqueIdException</code> without detail
     * message.
     */
    public NoUniqueIdException() {
    }

    /**
     * Constructs an instance of <code>NoUniqueIdException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoUniqueIdException(String msg) {
        super(msg);
    }
}
