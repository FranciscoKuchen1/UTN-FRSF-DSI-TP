package dsitp.backend.project.util;

public class BedelReferencedException extends RuntimeException {

    private final Integer reservaId;

    /**
     * Creates a new instance of <code>BedelReferencedException</code> without
     * detail message.
     */
    public BedelReferencedException() {
        this.reservaId = null;
    }

    /**
     * Constructs an instance of <code>BedelReferencedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BedelReferencedException(String msg) {
        super(msg);
        this.reservaId = null;
    }

    public BedelReferencedException(Integer reservaId) {
        super("El Bedel tiene reservas asociadas y no puede ser modificado/eliminado.");
        this.reservaId = reservaId;
    }

    public Integer getReservaId() {
        return reservaId;
    }
}
