package sh.leroy.axel.commechezsoi.awslambda.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Site {
    Leboncoin("Leboncoin"),
    LogicImmo("LogicImmo"),
    SeLoger("SeLoger"),
    PaP("PaP"),
    BienIci("Bien'ici");

    private final String text;

    Site(final String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }
}
