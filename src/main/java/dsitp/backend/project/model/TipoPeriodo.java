package dsitp.backend.project.model;

public enum TipoPeriodo {
    PRIMER_CUATRIMESTRE,
    SEGUNDO_CUATRIMESTRE,
    ANUAL;

    public Integer toInteger() {
        return this.ordinal();
    }

    public static TipoPeriodo fromInteger(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("El valor no puede ser nulo");
        }
        TipoPeriodo[] values = TipoPeriodo.values();
        if (value < 0 || value >= values.length) {
            throw new IllegalArgumentException("Valor inválido para TipoPeriodo: " + value);
        }
        return values[value];
    }
}
