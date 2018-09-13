package sh.leroy.axel.commechezsoi.awslambda.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnnonceurType {
    Particulier("Particulier"),
    Agence("Agence"),
    Both("Les deux");

    private final String text;

    AnnonceurType(final String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }
}
