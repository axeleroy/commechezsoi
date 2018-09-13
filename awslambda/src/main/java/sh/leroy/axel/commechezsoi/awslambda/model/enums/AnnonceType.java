package sh.leroy.axel.commechezsoi.awslambda.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnnonceType {
    Location("Location"),
    Vente("Vente");

    private final String text;

    AnnonceType(final String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }
}
