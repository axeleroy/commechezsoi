import { TestBed, inject } from '@angular/core/testing';

import { CriteresService } from './criteres.service';

describe('CriteresService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CriteresService]
    });
  });

  it('should be created', inject([CriteresService], (service: CriteresService) => {
    expect(service).toBeTruthy();
  }));
});
