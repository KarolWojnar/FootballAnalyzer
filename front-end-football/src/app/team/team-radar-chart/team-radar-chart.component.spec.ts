import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamRadarChartComponent } from './team-radar-chart.component';

describe('TeamRadarChartComponent', () => {
  let component: TeamRadarChartComponent;
  let fixture: ComponentFixture<TeamRadarChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TeamRadarChartComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeamRadarChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
