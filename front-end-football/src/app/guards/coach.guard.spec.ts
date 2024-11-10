import { TestBed } from '@angular/core/testing';

import { CoachGuard } from './coach.guard';

describe('CoachGuard', () => {
  let guard: CoachGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(CoachGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
