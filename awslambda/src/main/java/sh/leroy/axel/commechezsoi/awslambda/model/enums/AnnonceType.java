package sh.leroy.axel.commechezsoi.awslambda.model.enums;

public enum AnnonceType {
    Location("Location"),
    Vente("Vente");

    private final String text;

    AnnonceType(final String text) {
        this.text = text;
    }
}
