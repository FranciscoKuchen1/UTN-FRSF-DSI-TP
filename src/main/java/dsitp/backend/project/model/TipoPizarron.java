package dsitp.backend.project.model;

public enum TipoPizarron {
    VERDE,
    BLANCO;
    

    public Integer toInteger() {
        return this.ordinal();
    }

    public static TipoPizarron fromInteger(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("El valor no puede ser nulo");
        }
        TipoPizarron[] values = TipoPizarron.values();
        if (value < 0 || value >= values.length) {
            throw new IllegalArgumentException("Valor inválido para tipoPizarron: " + value);
        }
        return values[value];
    }
}

