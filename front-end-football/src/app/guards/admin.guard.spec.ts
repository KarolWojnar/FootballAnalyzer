import { TestBed } from '@angular/core/testing';

import { ADMINGuard } from './admin.guard';

describe('ADMINGuard', () => {
  let guard: ADMINGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(ADMINGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
