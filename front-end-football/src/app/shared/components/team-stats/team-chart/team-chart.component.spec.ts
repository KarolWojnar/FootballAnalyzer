import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamChartComponent } from './team-chart.component';

describe('TeamChartComponent', () => {
  let component: TeamChartComponent;
  let fixture: ComponentFixture<TeamChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeamChartComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TeamChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
