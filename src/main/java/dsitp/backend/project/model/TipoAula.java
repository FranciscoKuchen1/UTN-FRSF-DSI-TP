package dsitp.backend.project.model;

public enum TipoAula {
    SIN_RECURSOS_ADICIONALES,
    INFORMATICA,
    MULTIMEDIO;

    public Integer toInteger() {
        return this.ordinal();
    }

    public static TipoAula fromInteger(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("El valor no puede ser nulo");
        }
        TipoAula[] values = TipoAula.values();
        if (value < 0 || value >= values.length) {
            throw new IllegalArgumentException("Valor inválido para TipoAula: " + value);
        }
        return values[value];
    }

}
