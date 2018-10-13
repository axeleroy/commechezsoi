import {Component, Injectable, OnInit, Renderer2} from '@angular/core';
import {CriteresService} from "../../../services/criteres/criteres.service";
import {Criteres} from "../../../model/criteres/criteres";
import {City} from "../../../model/criteres/city";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import {of} from "rxjs/observable/of";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/merge';
import codesPostaux = require('codes-postaux');

const SEARCH_URL = "https://vicopo.selfbuild.fr/cherche/";

@Injectable()
export class CitySearchService {
  constructor(private http: HttpClient) {}

  search(term: string) {
    if (term === '') {
      return of([]);
    }

    return this.http
      .get(SEARCH_URL + term)
      .map( result => result['cities']);
  }
}

@Component({
  selector: 'app-cities',
  templateUrl: './cities.component.html',
  styleUrls: ['./cities.component.css'],
  providers: [CitySearchService]
})
export class CitiesComponent implements OnInit {
  city: any;
  criteres: Criteres;

  constructor(private criteresService: CriteresService,
              private service: CitySearchService,
              private _renderer: Renderer2) { }

  ngOnInit() {
    this.criteres = this.criteresService.criteres;
  }

  search = (text$: Observable<string>) =>
    text$
      .debounceTime(300)
      .distinctUntilChanged()
      .switchMap(term =>
        this.service.search(term)
          .map(cities => cities.slice(0, 20))
      )
  ;

  selectItem($event: any, input) {
    $event.preventDefault();
    const item = $event.item;
    let insee = codesPostaux.find(item.code)[0].codeCommune;
    insee = insee.slice(0, 2) + "0" + insee.slice(2); // Ajout d'un 0 supplémentaire pour SeLoger
    this.addCity(item.city, item.code, insee);
    this._renderer.setProperty(input, 'value', '');
  }

  addCity(name: string, postcode: number, insee: number): void {
    if (name && postcode && insee) {
      const city = new City();
      city.name = name.toLowerCase();
      city.postcode = postcode;
      city.insee = insee;

      this.criteres.cities.push(city);
      this.update();
    }
  }

  removeCity(city: City): void {
    const index = this.criteres.cities.indexOf(city);
    if (index !== -1) {
      this.criteres.cities.splice(index, 1);
      this.update();
    }
  }

  update(): void {
    this.criteresService.criteresChanged();
  }
}
