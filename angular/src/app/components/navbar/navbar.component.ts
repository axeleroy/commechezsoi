import { Component, OnInit } from '@angular/core';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CriteresService} from "../../services/criteres/criteres.service";
import {AnnoncesService} from "../../services/annonces/annonces.service";
import {Criteres} from "../../model/criteres/criteres";
import {Annonce} from "../../model/annonce/annonce";

@Component({
  selector: 'ngbd-modal-content',
  templateUrl: './about.component.html'
})
export class AboutContent {
  constructor(public activeModal: NgbActiveModal) {}
}

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isNavbarCollapsed=true;

  constructor(private modalService: NgbModal,
              public criteresService: CriteresService,
              public annoncesService: AnnoncesService) { }

  ngOnInit() {
  }

  about(): void {
    this.modalService.open(AboutContent, {size: "lg"});
  }

}
