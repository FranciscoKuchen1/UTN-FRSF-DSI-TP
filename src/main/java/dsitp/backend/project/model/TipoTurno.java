package dsitp.backend.project.model;

public enum TipoTurno {
    MANANA,
    TARDE,
    NOCHE;

    public Integer toInteger() {
        return this.ordinal();
    }

    public static TipoTurno fromInteger(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("El valor no puede ser nulo");
        }
        TipoTurno[] values = TipoTurno.values();
        if (value < 0 || value >= values.length) {
            throw new IllegalArgumentException("Valor inválido para TipoTurno: " + value);
        }
        return values[value];
    }
}
