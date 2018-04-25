import { Injectable } from '@angular/core';
import {CriteresService} from '../criteres/criteres.service';
import {AnnoncesService} from '../annonces/annonces.service';
import {Annonce} from '../../model/annonce/annonce';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable()
export class FetchService {

  constructor(private parametersService: CriteresService,
              private annoncesService: AnnoncesService,
              private client: HttpClient) { }

  fetch(): void {
    if (this.parametersService.leboncoin) {
      this.fetchLeboncoin();
    }
    if (this.parametersService.logicimmo) {

    }
    if (this.parametersService.pap) {

    }
    if (this.parametersService.seloger) {

    }
  }

  fetchLeboncoin(): void {
    this.client.post<String[]>(environment.aws_lambda_endpoint + 'leboncoinlist', this.parametersService.criteres)
      .subscribe(ids => {
        for (const id of ids) {
          this.client.post<Annonce>(environment.aws_lambda_endpoint + 'leboncoinview', id)
            .subscribe(annonce => this.annoncesService.add(annonce));
        }
      });
  }

}
