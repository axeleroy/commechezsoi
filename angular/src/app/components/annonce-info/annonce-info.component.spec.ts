import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnonceInfoComponent } from './annonce-info.component';

describe('AnnonceInfoComponent', () => {
  let component: AnnonceInfoComponent;
  let fixture: ComponentFixture<AnnonceInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnnonceInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnnonceInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
