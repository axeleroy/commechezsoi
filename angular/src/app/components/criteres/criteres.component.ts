import {Component, NgModule, OnInit} from '@angular/core';
import {CriteresService} from '../../services/criteres/criteres.service';
import {AnnonceType} from '../../model/enums/annoncetype';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {AnnonceurType} from "../../model/enums/annonceur";

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
  AnnonceurType = AnnonceurType;

  constructor(public criteresService: CriteresService) {
  }

  ngOnInit() {
  }
}
