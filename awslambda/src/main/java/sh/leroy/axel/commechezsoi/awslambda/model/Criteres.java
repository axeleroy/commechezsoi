package sh.leroy.axel.commechezsoi.awslambda.model;

import java.util.Arrays;

import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceType;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceurType;

public class Criteres {

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
    public AnnonceurType annonceur;

    public Criteres() {
        super();
    }

    public Criteres(City[] cities, int minPrice, int maxPrice,
                      int minSurface, int maxSurface, int minRooms, int maxRooms,
                      int minBedrooms, int maxBedrooms, AnnonceType type, AnnonceurType annonceur) {
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
        this.annonceur = annonceur;
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
