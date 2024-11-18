import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { TeamStatsForm } from '../../models/forms/forms.model';
import { FormService } from '../../services/form/form.service';
import { ApiService } from '../../services/api.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-oponent',
  templateUrl: './oponent.component.html',
  styleUrls: ['./oponent.component.scss'],
})
export class OponentComponent implements OnInit {
  teamStats = JSON.parse(localStorage.getItem('teamOpponent')!);
  form: FormGroup<TeamStatsForm> = this.formService.initOpponentStatsForm();
  sub!: Subscription;
  alertMessage = '';

  constructor(
    private formService: FormService,
    private apiService: ApiService,
  ) {}

  handleSubmit() {
    this.form.setValue(this.form.getRawValue());
    this.fetchTeamData();
  }

  ngOnInit(): void {
    this.fetchTeamData();
  }

  fetchTeamData() {
    this.sub = this.apiService.fetchTeamOpponent(this.form.value).subscribe({
      next: (teamStats) => {
        this.teamStats = teamStats;
        localStorage.setItem('teamOpponent', JSON.stringify(teamStats));
        localStorage.setItem(
          'startDateOpponent',
          JSON.stringify(this.form.value.startDate),
        );
        localStorage.setItem(
          'endDateOpponent',
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
}
