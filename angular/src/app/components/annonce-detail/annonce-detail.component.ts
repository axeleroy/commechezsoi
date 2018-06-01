import {Component, Input, OnInit} from '@angular/core';
import {Annonce} from "../../model/annonce/annonce";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {AnnoncesService} from "../../services/annonces/annonces.service";

@Component({
  selector: 'app-annonce-detail',
  templateUrl: './annonce-detail.component.html',
  styleUrls: ['./annonce-detail.component.css']
})
export class AnnonceDetailComponent implements OnInit {
  @Input() annonce: Annonce;

  constructor(public activeModal: NgbActiveModal,
              public annoncesService: AnnoncesService) { }

  ngOnInit() {
    console.log(this.annonce)
  }

  delete(): void {
    this.annoncesService.remove(this.annonce);
    this.activeModal.close('Close click')
  }
}
