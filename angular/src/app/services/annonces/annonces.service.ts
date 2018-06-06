import {ApplicationRef, EventEmitter, Injectable} from '@angular/core';
import {Annonce, Site} from '../../model/annonce/annonce';
import {PersistenceService, StorageType} from 'angular-persistence';
import {AnnonceDatabase} from "../../model/annonce/annoncedb";
import {Observable} from "rxjs/Observable";
import 'dexie-observable';
import 'rxjs/add/observable/fromPromise';

@Injectable()
export class AnnoncesService {
  private deleted: string[];
  changeDetectionEmitter: EventEmitter<void> = new EventEmitter<void>();

  constructor(private persistenceService: PersistenceService,
              private db: AnnonceDatabase) {

    try {
      this.deleted = this.persistenceService.get('deleted', StorageType.LOCAL);
    } catch (e) {
      console.log(e);
    }
    if (this.deleted == null)
      this.deleted = [];
  }

  getAll() {
    return Observable.fromPromise(this.db.annonces.orderBy('created').reverse().toArray());
  }

  get(id: string) {
    return this.db.annonces
      .where('id').equals(id);
  }

  add(annonce: Annonce) {
    if (!this.deleted.includes(annonce.id))
      this.db.transaction('rw', this.db.annonces, () => {
        this.db.annonces.put(annonce)
          .then(() => this.changeDetectionEmitter.emit())
          .catch(e => { console.log(e) });
      });
  }

  remove(annonce: Annonce) {
    this.deleted.push(annonce.id);
    this.updateDeleted();
    this.db.transaction('rw', this.db.annonces, () => {
      this.db.annonces.delete(annonce.id)
        .then(() => this.changeDetectionEmitter.emit())
        .catch(e => console.log(e));
    });
  }

  clear() {
    this.deleted = [];
    this.updateDeleted();
    this.db.annonces.clear()
      .then(() => this.changeDetectionEmitter.emit());
  }

  updateDeleted(): void {
    try {
      this.persistenceService.set('deleted', this.deleted, {type: StorageType.LOCAL});
    } catch (e) {
      console.log(e);
    }
  }
}
