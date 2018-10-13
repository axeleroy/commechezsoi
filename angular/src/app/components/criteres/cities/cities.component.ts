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

const SEARCH_URL = "https://res.bienici.com/suggest.json?q=";

@Injectable()
export class CitySearchService {
  constructor(private http: HttpClient) {}

  search(term: string) {
    if (term === '') {
      return of([]);
    }

    return this.http
      .get<any[]>(SEARCH_URL + term);
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
    this.addCity(item.name, item.postalCodes[0], item.insee_code, item.zoneIds[0]);
    this._renderer.setProperty(input, 'value', '');
  }

  addCity(name: string, postcode: number, insee: number, zoneId: string): void {
    if (name && postcode && insee) {
      const city = new City();
      city.name = name;
      city.postcode = postcode;
      city.insee = insee;
      city.zoneId = zoneId;

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
