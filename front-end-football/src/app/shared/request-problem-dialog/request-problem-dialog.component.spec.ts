import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestProblemDialogComponent } from './request-problem-dialog.component';

describe('RequestProblemDialogComponent', () => {
  let component: RequestProblemDialogComponent;
  let fixture: ComponentFixture<RequestProblemDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequestProblemDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RequestProblemDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
