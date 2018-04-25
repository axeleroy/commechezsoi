import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {AnnoncesService} from '../../services/annonces/annonces.service';
import {Annonce} from '../../model/annonce/annonce';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AnnonceDetailComponent} from '../annonce-detail/annonce-detail.component';

@Component({
  selector: 'app-annonces-list',
  templateUrl: './annonces-list.component.html',
  styleUrls: ['./annonces-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AnnoncesListComponent implements OnInit {
  annonces = [];

  constructor(public annoncesService: AnnoncesService,
              private modalService: NgbModal,
              private cdr: ChangeDetectorRef) {
  }

  ngOnInit() {
    this.update();

    this.annoncesService.changeDetectionEmitter.subscribe(() => {
        this.update()
      }
    );
  }

  update() {
    this.annoncesService.getAll().subscribe( data => {
      this.annonces = data;
      this.cdr.detectChanges();
    });
  }

  open(annonce: Annonce): void {
    const modalRef = this.modalService.open(AnnonceDetailComponent, {size: 'lg'});
    modalRef.componentInstance.annonce = annonce;
  }

  delete($event, annonce): void {
    $event.stopPropagation();
    this.annoncesService.remove(annonce);
  }
}
