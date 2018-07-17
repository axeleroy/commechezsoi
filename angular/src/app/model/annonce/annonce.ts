import {AnnonceurType} from "../enums/annonceur";
import {AnnonceType} from "../enums/annoncetype";
import {Site} from "../enums/site";

export interface Annonce {
  id: string;
  site: Site;
  created: Date;
  title: string;
  city: string;
  price: number;
  surface: number;
  description?: string;
  telephone?: string;
  charges?: number;
  rooms: number;
  bedrooms?: number;
  link: string;
  pictures?: string[];
  type: AnnonceType;
  annonceur: AnnonceurType;
}


