import { Component } from '@angular/core';
import {PersistenceService, StorageType} from "angular-persistence";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  closed: boolean;

  constructor(protected service: PersistenceService) {
    try {
      this.closed = this.service.get('alertClosed', StorageType.LOCAL);
      if (this.closed == null) {
        this.closed = false;
        this.service.set('alertClosed', this.closed, {type: StorageType.LOCAL});
      }
    } catch (e) {
      console.log(e);
      this.closed = false;
    }
  }

  setClosed() {
    this.closed = true;
    this.service.set('alertClosed', this.closed, { type: StorageType.LOCAL });
  }

  lsTest(): boolean {
    let test = 'test';
    try {
      localStorage.setItem(test, test);
      localStorage.removeItem(test);
      return true;
    } catch (e) {
      return false;
    }
  }

  dbTest():boolean {
    return !window.indexedDB;
  }
}
