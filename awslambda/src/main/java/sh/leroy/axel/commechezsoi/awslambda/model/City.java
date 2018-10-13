package sh.leroy.axel.commechezsoi.awslambda.model;

public class City {
    public String name;
    public int postcode;
    public String insee;
    public String zoneId;

    public City() {
        super();
    }

    public City(String name, int postcode, String insee, String zoneId) {
        this.name = name;
        this.postcode = postcode;
        this.insee = insee;
        this.zoneId = zoneId;
    }

    @Override public String toString() {
        return "City{" + "name='" + name + '\'' + ", postcode=" + postcode + ", insee='" + insee + ", zoneId='"
                + zoneId + '\'' + '}';
    }
}
