package sh.leroy.axel.commechezsoi.awslambda.model.enums;

public enum AnnonceurType {
    Particulier("Particulier"),
    Agence("Agence"),
    Both("Les deux");

    private final String text;

    AnnonceurType(final String text) {
        this.text = text;
    }
}
