package model;

public enum Currency {
    USD("USD", "Dolar"),
    ARS("ARS", "Peso argentino"),
    BRL("BRL", "Real brasileño"),
    COP("COP", "Peso colombiano");

    private final String code;
    private final String displayName;

    Currency(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }
}

