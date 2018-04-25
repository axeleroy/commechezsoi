package sh.leroy.axel.commechezsoi.awslambda.model;

public class City {
    public String name;
    public int postcode;
    public String insee;

    public City() {
        super();
    }

    public City(String name, int postcode, String insee) {
        this.name = name;
        this.postcode = postcode;
        this.insee = insee;
    }

    @Override public String toString() {
        return "City{" + "name='" + name + '\'' + ", postcode=" + postcode + ", insee='" + insee + '\'' + '}';
    }
}
