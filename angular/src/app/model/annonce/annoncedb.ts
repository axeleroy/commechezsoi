import Dexie from "dexie";
import {Annonce} from "./annonce";
import 'dexie-observable';

export class AnnonceDatabase extends Dexie {
  annonces: Dexie.Table<Annonce, string>;

  constructor () {
    super("AnnonceDatabase");
    this.version(1).stores({
      annonces: '&id, created'
    });
  }
}
