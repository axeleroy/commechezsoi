import {City} from './city';
import {AnnonceurType} from "../enums/annonceur";
import {AnnonceType} from "../enums/annoncetype";

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
  annonceur: AnnonceurType;

  constructor(obj?) {
    if (obj) {
      Object.assign(this, obj);
    } else {
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
      // FIXME: Si Both, le type d'annonce n'est pas sélectionné sur la home
      this.annonceur = AnnonceurType.Agence;
    }
  };
}


