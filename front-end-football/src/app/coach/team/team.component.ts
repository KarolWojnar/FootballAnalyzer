import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Stats } from '../../models/stats';
import { ApiService } from '../../services/api.service';
import { TeamStatsForm } from '../../models/forms/forms.model';
import { FormService } from '../../services/form/form.service';
import { FormGroup } from '@angular/forms';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss'],
})
export class TeamComponent implements OnInit {
  teamStats: Stats = JSON.parse(localStorage.getItem('teamStats')!);
  form: FormGroup<TeamStatsForm> = this.formService.initTeamStatsForm();
  formVisible: boolean = true;
  sub!: Subscription;
  selectedChart = 'line';
  isDarkMode = true;
  logoUrl = localStorage.getItem('logoUrl')!;
  alertMessage = '';
  constructor(
    private apiService: ApiService,
    private formService: FormService,
    private themeService: ThemeService,
  ) {
    this.themeService.darkMode$.subscribe((theme) => {
      this.isDarkMode = theme;
    });
  }

  ngOnInit(): void {
    if (localStorage.getItem('teamStats') == null) {
      this.fetchTeamData();
    }
  }

  fetchTeamData() {
    this.sub = this.apiService.fetchTeamData(this.form.value).subscribe({
      next: (teamStats) => {
        this.teamStats = teamStats;
        localStorage.setItem('teamStats', JSON.stringify(teamStats));
        this.selectedChart = 'line';
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
