import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Stats } from '../../models/stats';
import { ApiService } from '../../services/api.service';
import { TeamStatsForm } from '../../models/forms/forms.model';
import { FormService } from '../../services/form/form.service';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
})
export class TeamComponent {
  teamStats: Stats = JSON.parse(localStorage.getItem('teamStats')!);
  form: FormGroup<TeamStatsForm> = this.formService.initTeamStatsForm();
  formVisible: boolean = true;
  sub!: Subscription;
  selectedChart = 'line';

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private formService: FormService,
  ) {}

  fetchTeamData() {
    this.sub = this.apiService.fetchTeamData(this.form.value).subscribe({
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
