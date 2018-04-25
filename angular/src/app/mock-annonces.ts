import { Annonce, Site } from "./model/annonce/annonce";

export const ANNONCES: Annonce[] = [
  {
    id: 'lbc-2',
    site: Site.Leboncoin,
    created: new Date(),
    title: 'Duplex 4 pièces',
    city: 'Montrouge',
    price: 900,
    surface: 35,
    description: 'Lorem Ipsum',
    telephone: '0123456789',
    charges: 100,
    rooms: 4,
    bedrooms: 2,
    link: 'https://google.fr',
    pictures: ['http://via.placeholder.com/450x150']
  },
  {
    id: 'li-2',
    site: Site.LogicImmo,
    created: new Date(),
    title: 'Duplex 4 pièces',
    city: 'Montrouge',
    price: 900,
    surface: 35,
    description: 'Lorem Ipsum',
    telephone: '0123456789',
    charges: 100,
    rooms: 4,
    bedrooms: 2,
    link: 'https://google.fr',
    pictures: ['http://via.placeholder.com/350x150']
  },
  {
    id: 'lbc-1',
    site: Site.Leboncoin,
    created: new Date('December 17, 1995 03:24:00'),
    title: 'Superbe 3 pièces à Nanterre',
    city: 'Nanterre',
    price: 800,
    surface: 25,
    description: 'Lorem Ipsum',
    telephone: '0123456789',
    charges: 100,
    rooms: 2,
    bedrooms: 1,
    link: 'https://google.fr',
    pictures: ['http://via.placeholder.com/350x150']
  },
  {
    id: 'li-1',
    site: Site.LogicImmo,
    created: new Date('December 18, 1995 03:24:00'),
    title: 'Duplex 4 pièces',
    city: 'Montrouge',
    price: 900,
    surface: 35,
    description: 'Lorem Ipsum',
    telephone: '0123456789',
    charges: 100,
    rooms: 4,
    bedrooms: 2,
    link: 'https://google.fr',
    pictures: ['http://via.placeholder.com/400x200', 'http://via.placeholder.com/400x200']
  },
  {
    id: 'li-3',
    site: Site.LogicImmo,
    created: new Date(),
    title: 'Duplex 4 pièces',
    city: 'Montrouge',
    price: 900,
    surface: 35,
    description: 'Lorem Ipsum',
    telephone: '0123456789',
    charges: null,
    rooms: 4,
    bedrooms: null,
    link: 'https://google.fr',
    pictures: ['http://via.placeholder.com/350x150']
  }
];


