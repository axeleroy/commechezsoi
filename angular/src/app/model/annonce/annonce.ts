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
}

export enum Site {
  Leboncoin,
  LogicImmo,
  SeLoger,
  PaP
}
