import {City} from './city';

export class Criteres {
  cities: City[];
  minPrice: number;
  maxPrice: number;
  minSurface: number;
  maxSurface: number;
  minRooms: number;
  maxRooms: number;
  minBedrooms: number;
  maxBedrooms: number;
  type: AnnonceType;

  constructor() {
    this.cities = [];
    this.minPrice = 0;
    this.maxPrice = 0;
    this.minSurface = 0;
    this.maxSurface = 0;
    this.minRooms = 0;
    this.maxRooms = 0;
    this.minBedrooms = 0;
    this.maxBedrooms = 0;
    this.type = AnnonceType.Location;
  };

  public setFrom(o: Criteres) {
    this.cities = o.cities;
    this.minPrice = o.minPrice;
    this.maxPrice = o.maxPrice;
    this.minSurface = o.minSurface;
    this.maxSurface = o.maxSurface;
    this.minRooms = o.minRooms;
    this.maxRooms = o.maxRooms;
    this.minBedrooms = o.minBedrooms;
    this.maxBedrooms = o.maxBedrooms;
    this.type = o.type;
  }
}

export enum AnnonceType {
  Location,
  Vente
}
