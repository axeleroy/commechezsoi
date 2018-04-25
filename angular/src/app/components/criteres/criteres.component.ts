import {Component, NgModule, OnInit} from '@angular/core';
import {CriteresService} from '../../services/criteres/criteres.service';
import {AnnonceType} from '../../model/criteres/criteres';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-criteres',
  templateUrl: './criteres.component.html',
  styleUrls: ['./criteres.component.css']
})
@NgModule({
  imports: [ NgbModule ]
})
export class CriteresComponent implements OnInit {
  AnnonceType = AnnonceType;
  annonceTypes = (<any>Object).values(AnnonceType).filter( e => typeof( e ) == 'number' );

  constructor(public criteresService: CriteresService) {
  }

  ngOnInit() {
  }
}
