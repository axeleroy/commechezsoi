package sh.leroy.axel.commechezsoi.awslambda.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Annonce {
    public enum Site { Leboncoin, LogicImmo, SeLoger, PaP }

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

    public Annonce() {
        super();
    }

    public Annonce(String id, Site site, Date created, String title, String city,
                   double price, double surface, String description, String telephone,
                   double charges, int rooms, int bedrooms, String link, String[] pictures) {
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

        public Annonce build() {
            return new Annonce(id, site, created, title, city, price, surface, description, telephone, charges, rooms, bedrooms, link, pictures);
        }
    }

}
