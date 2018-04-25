package sh.leroy.axel.commechezsoi.awslambda.model;

import java.util.Arrays;

public class Criteres {
    public enum AnnonceType {
        Location(0), Vente(1);
        private int type;
        private AnnonceType(int type) {
            this.type = type;
        }
        public int getType() {
            return this.type;
        }
    }

    public City[] cities;
    public int minPrice;
    public int maxPrice;
    public int minSurface;
    public int maxSurface;
    public int minRooms;
    public int maxRooms;
    public int minBedrooms;
    public int maxBedrooms;
    public AnnonceType type;

    public Criteres() {
        super();
    }

    public Criteres(City[] cities, int minPrice, int maxPrice,
                      int minSurface, int maxSurface, int minRooms, int maxRooms,
                      int minBedrooms, int maxBedrooms, AnnonceType type,
                      boolean leboncoin, boolean logicimmo, boolean pap, boolean seloger) {
        this.cities = cities;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minSurface = minSurface;
        this.maxSurface = maxSurface;
        this.minRooms = minRooms;
        this.maxRooms = maxRooms;
        this.minBedrooms = minBedrooms;
        this.maxBedrooms = maxBedrooms;
        this.type = type;
    }

    // region utils
    public String[] getZipcodes() {
        return Arrays.stream(cities).map(city -> city.postcode).map(String::valueOf).toArray(String[]::new);
    }

    public String[] getCitiesNames() {
        return Arrays.stream(cities).map(city -> city.name).toArray(String[]::new);
    }

    public String[] getInsees() {
        return Arrays.stream(cities).map(city -> city.insee).toArray(String[]::new);
    }
    // endregion

    @Override public String toString() {
        return "Criteres{" + "cities=" + Arrays.toString(cities) + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice + ", minSurface=" + minSurface + ", maxSurface=" + maxSurface + ", minRooms="
                + minRooms + ", maxRooms=" + maxRooms + ", minBedrooms=" + minBedrooms + ", maxBedrooms=" + maxBedrooms + ", type=" + type + '}';
    }
}
