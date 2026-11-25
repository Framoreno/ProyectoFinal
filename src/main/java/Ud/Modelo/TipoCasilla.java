package Ud.Modelo;

public enum TipoCasilla {
    LIBRE(true),
    PARED(false),
    PUERTA(true),
    LLAVE(true),
    TESORO(true);

    private final boolean transitable;

    TipoCasilla(boolean transitable) {
        this.transitable = transitable;
    }

    public boolean esTransitable() {
        return transitable;
    }
}
