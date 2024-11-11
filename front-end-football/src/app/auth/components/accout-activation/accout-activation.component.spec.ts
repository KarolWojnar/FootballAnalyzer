import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccoutActivationComponent } from './accout-activation.component';

describe('AccoutActivationComponent', () => {
  let component: AccoutActivationComponent;
  let fixture: ComponentFixture<AccoutActivationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccoutActivationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccoutActivationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
