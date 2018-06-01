import {Component, Input, OnInit} from '@angular/core';
import {Annonce} from "../../model/annonce/annonce";

@Component({
  selector: 'app-annonce-info',
  templateUrl: './annonce-info.component.html',
  styleUrls: ['./annonce-info.component.css']
})
export class AnnonceInfoComponent implements OnInit {
  @Input() annonce: Annonce;
  @Input() isDetail = false;

  constructor() { }

  ngOnInit() {
  }

}
