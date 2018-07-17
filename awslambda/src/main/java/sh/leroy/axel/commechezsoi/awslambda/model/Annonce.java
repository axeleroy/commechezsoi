package sh.leroy.axel.commechezsoi.awslambda.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceType;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.AnnonceurType;
import sh.leroy.axel.commechezsoi.awslambda.model.enums.Site;

public class Annonce {

    public String id;
    public Site site;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public Date created;
    public String title;
    public String city;
    public double price;
    public double surface;
    public String description;
    public String telephone;
    public double charges;
    public int rooms;
    public int bedrooms;
    public String link;
    public String[] pictures;
    public AnnonceurType annonceur;
    public AnnonceType type;

    public Annonce() {
        super();
    }

    public Annonce(String id, Site site, Date created, String title, String city,
            double price, double surface, String description, String telephone,
            double charges, int rooms, int bedrooms, String link, String[] pictures,
            AnnonceurType annonceur, AnnonceType type) {
        this.id = id;
        this.site = site;
        this.created = created;
        this.title = title;
        this.city = city;
        this.price = price;
        this.surface = surface;
        this.description = description;
        this.telephone = telephone;
        this.charges = charges;
        this.rooms = rooms;
        this.bedrooms = bedrooms;
        this.link = link;
        this.pictures = pictures;
        this.type = type;
        this.annonceur = annonceur;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private Site site;
        private Date created;
        private String title;
        private String city;
        private double price;
        private double surface;
        private String description;
        private String telephone;
        private double charges;
        private int rooms;
        private int bedrooms;
        private String link;
        private String[] pictures;
        private AnnonceurType annonceur;
        private AnnonceType type;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setSite(Site site) {
            this.site = site;
            return this;
        }

        public Builder setCreated(Date created) {
            this.created = created;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder setSurface(double surface) {
            this.surface = surface;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setTelephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public Builder setCharges(double charges) {
            this.charges = charges;
            return this;
        }

        public Builder setRooms(int rooms) {
            this.rooms = rooms;
            return this;
        }

        public Builder setBedrooms(int bedrooms) {
            this.bedrooms = bedrooms;
            return this;
        }

        public Builder setLink(String link) {
            this.link = link;
            return this;
        }

        public Builder setPictures(String[] pictures) {
            this.pictures = pictures;
            return this;
        }

        public Builder setAnnonceur(AnnonceurType annonceur) {
            this.annonceur = annonceur;
            return this;
        }

        public Builder setType(AnnonceType type) {
            this.type = type;
            return this;
        }

        public Annonce build() {
            return new Annonce(id, site, created, title, city, price,
                    surface, description, telephone, charges, rooms,
                    bedrooms, link, pictures, annonceur, type);
        }
    }

}
