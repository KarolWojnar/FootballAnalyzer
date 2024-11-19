import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayerRadarModalComponent } from './player-radar-modal.component';

describe('PlayerRadarModalComponent', () => {
  let component: PlayerRadarModalComponent;
  let fixture: ComponentFixture<PlayerRadarModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PlayerRadarModalComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(PlayerRadarModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
