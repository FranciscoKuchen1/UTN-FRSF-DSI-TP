package dsitp.backend.project.util;

public class BedelNotFoundException extends RuntimeException {

    private final Integer reservaId;

    /**
     * Creates a new instance of <code>BedelNotFoundException</code> without
     * detail message.
     */
    public BedelNotFoundException() {
        this.reservaId = null;
    }

    /**
     * Constructs an instance of <code>BedelNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BedelNotFoundException(String msg) {
        super(msg);
        this.reservaId = null;
    }

    public BedelNotFoundException(Integer reservaId) {
        super("El Bedel no fue encontrado.");
        this.reservaId = reservaId;
    }

    public Integer getReservaId() {
        return reservaId;
    }
}
