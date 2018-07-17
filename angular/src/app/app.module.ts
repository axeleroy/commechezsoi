import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from "@angular/forms";
import {PersistenceModule} from 'angular-persistence';
import {HttpClientModule} from "@angular/common/http";
import {registerLocaleData} from '@angular/common';
import localeFr from '@angular/common/locales/fr';

import {AppComponent} from './components/app.component';
import {CriteresComponent} from './components/criteres/criteres.component';
import {CriteresService} from './services/criteres/criteres.service';
import {AnnoncesListComponent} from './components/annonces-list/annonces-list.component';
import {AnnoncesService} from './services/annonces/annonces.service';
import {AnnonceDetailComponent} from './components/annonce-detail/annonce-detail.component';
import {AboutContent, NavbarComponent} from './components/navbar/navbar.component';
import {FetchComponent} from './components/fetch/fetch.component';
import {FetchService} from "./services/fetch/fetch.service";
import {CitiesComponent} from './components/criteres/cities/cities.component';
import {OrderAnnonceByDatePipe} from './pipes/order-by-date.pipe';
import {AnnonceDatabase} from "./model/annonce/annoncedb";
import { SafeHtmlPipe } from './pipes/safe-html.pipe';
import { AnnonceInfoComponent } from './components/annonce-info/annonce-info.component';
import { KeysPipe } from './pipes/keys.pipe';

registerLocaleData(localeFr, 'fr');

@NgModule({
  declarations: [
    AppComponent,
    CriteresComponent,
    AnnoncesListComponent,
    AnnonceDetailComponent,
    NavbarComponent,
    AboutContent,
    FetchComponent,
    CitiesComponent,
    OrderAnnonceByDatePipe,
    SafeHtmlPipe,
    AnnonceInfoComponent,
    KeysPipe,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    PersistenceModule,
    HttpClientModule,
    NgbModule.forRoot()
  ],
  providers: [AnnonceDatabase, CriteresService, AnnoncesService, FetchService, NgbActiveModal],
  bootstrap: [AppComponent],
  entryComponents: [AnnonceDetailComponent, AboutContent]
})
export class AppModule {
}
