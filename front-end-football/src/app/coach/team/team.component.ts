import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Stats } from '../../models/stats';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
})
export class TeamComponent {
  teamStats: Stats = JSON.parse(localStorage.getItem('teamStats')!);
  form!: FormGroup;
  formVisible: boolean = true;
  sub!: Subscription;
  selectedChart = 'line';

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
  ) {
    this.form = this.fb.group({
      startDate: ['2022-09-11'],
      endDate: ['2024-12-02'],
      rounding: ['week'],
    });
  }

  fetchTeamData() {
    this.sub = this.apiService
      .fetchTeamData(
        this.form.value.startDate,
        this.form.value.endDate,
        this.form.value.rounding,
      )
      .subscribe({
        next: (teamStats) => {
          this.teamStats = teamStats;
          localStorage.setItem('teamStats', JSON.stringify(teamStats));
          this.selectedChart = 'line';
        },
        error: (err) => {
          console.log(err);
        },
      });
  }

  onSubmit(): void {
    this.fetchTeamData();
  }

  selectChart(chartType: string) {
    this.selectedChart = chartType;
  }

  toggleForm() {
    this.formVisible = !this.formVisible;
    this.teamStats = JSON.parse(localStorage.getItem('teamStats')!);
  }
}
