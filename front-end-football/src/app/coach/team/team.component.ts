import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Stats } from '../../models/stats';
import { ApiService } from '../../services/api.service';
import { TeamStatsForm } from '../../models/forms/forms.model';
import { FormService } from '../../services/form/form.service';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
})
export class TeamComponent implements OnInit {
  teamStats: Stats = JSON.parse(localStorage.getItem('teamStats')!);
  form: FormGroup<TeamStatsForm> = this.formService.initTeamStatsForm();
  isDarkMode = true;
  alertMessage = '';
  sub!: Subscription;
  constructor(
    private formService: FormService,
    private apiService: ApiService,
  ) {}

  ngOnInit(): void {
    this.fetchTeamData();
  }

  fetchTeamData() {
    this.sub = this.apiService.fetchTeamData(this.form.value).subscribe({
      next: (teamStats) => {
        this.teamStats = teamStats;
        localStorage.setItem('teamStats', JSON.stringify(teamStats));
        localStorage.setItem(
          'startDateTeam',
          JSON.stringify(this.form.value.startDate),
        );
        localStorage.setItem(
          'endDateTeam',
          JSON.stringify(this.form.value.endDate),
        );
      },
      error: (err) => {
        if (err.error.status === 403) {
          this.alertMessage = 'Brak uprawnień do przeglądania zawodników.';
          return;
        }

        this.alertMessage = 'Brak danych o drużynie.';
      },
    });
  }

  handleSubmit() {
    this.form.setValue(this.form.getRawValue());
    this.fetchTeamData();
  }
}
