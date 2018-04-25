import {Component, OnInit, Renderer2} from '@angular/core';
import {FetchService} from "../../services/fetch/fetch.service";

@Component({
  selector: 'app-fetch',
  templateUrl: './fetch.component.html',
  styleUrls: ['./fetch.component.css']
})
export class FetchComponent implements OnInit {

  constructor(private fetchService: FetchService,
              private renderer: Renderer2) { }

  ngOnInit() {
  }

  fetch($event, button): void {
    /*$event.preventDefault();

    this.renderer.setProperty(button, 'innerHTML', '<i class="fa fa-cog fa-spin"></i> Recherche en cours…');
    this.renderer.addClass(button, 'disabled');*/
    this.fetchService.fetch();
    /*setTimeout(() =>
      {
        this.renderer.setProperty(button, 'innerHTML', 'Lancer la recherche');
        this.renderer.removeClass(button, 'disabled');
      },
      5000);*/
  }

}
