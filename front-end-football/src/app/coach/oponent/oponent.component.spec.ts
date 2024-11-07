import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OponentComponent } from './oponent.component';

describe('OponentComponent', () => {
  let component: OponentComponent;
  let fixture: ComponentFixture<OponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OponentComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(OponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
