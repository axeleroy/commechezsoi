import { TestBed, inject } from '@angular/core/testing';

import { AnnoncesService } from './annonces.service';

describe('AnnoncesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AnnoncesService]
    });
  });

  it('should be created', inject([AnnoncesService], (service: AnnoncesService) => {
    expect(service).toBeTruthy();
  }));
});
