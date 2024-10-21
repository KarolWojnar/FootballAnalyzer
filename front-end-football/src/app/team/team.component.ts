import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ApiService } from '../services/api.service';
import { Subscription } from 'rxjs';
import { Stats } from '../models/stats';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
})
export class TeamComponent {
  teamStats!: Stats;
  form!: FormGroup;
  sub!: Subscription;
  selectedChart = 'line';

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
  ) {
    this.form = this.fb.group({
      teamName: ['Arsenal'],
      startDate: ['2022-09-11'],
      endDate: ['2022-12-02'],
      rounding: ['week'],
    });
  }

  fetchTeamData() {
    this.sub = this.apiService
      .fetchTeamData(
        this.form.value.teamName,
        this.form.value.startDate,
        this.form.value.endDate,
        this.form.value.rounding,
      )
      .subscribe({
        next: (teamStats) => {
          this.teamStats = teamStats;
          this.selectedChart = 'line';
        },
      });
  }

  onSubmit(): void {
    this.fetchTeamData();
  }

  selectChart(chartType: string) {
    this.selectedChart = chartType;
  }
}
