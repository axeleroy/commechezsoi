import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnonceDetailComponent } from './annonce-detail.component';

describe('AnnonceDetailComponent', () => {
  let component: AnnonceDetailComponent;
  let fixture: ComponentFixture<AnnonceDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnnonceDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnnonceDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
