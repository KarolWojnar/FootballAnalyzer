import { Component } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import { FormService } from '../../services/form/form.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NewLeagueForm } from '../../models/forms/forms.model';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-teams',
  templateUrl: './teams.component.html',
  styleUrls: ['./teams.component.scss'],
})
export class TeamsComponent {
  isDarkMode = true;
  showFeedback = false;
  feedbackMessage = '';
  teamInfo: { [key: string]: string | undefined };

  availableLeagues: any[] = [];
  selectedLeague: any = null;

  objectKeys = Object.keys;
  showNewLeagueForm = true;
  showCountryForm = true;
  showLeagueList = false;
  countryForm: FormGroup = this.initCountryForm();

  newLeagueForm: FormGroup<NewLeagueForm> =
    this.formService.initNewLeagueForm();
  showTeamList = false;
  availableTeams: any[] = [];

  constructor(
    private router: Router,
    private themeService: ThemeService,
    private formService: FormService,
    private fb: FormBuilder,
    private apiService: ApiService,
  ) {
    this.themeService.darkMode$.subscribe((isDarkMode) => {
      this.isDarkMode = isDarkMode;
    });
    this.teamInfo = history.state.teamInfo;
    console.log(this.teamInfo);
  }

  initCountryForm(): FormGroup {
    return this.fb.group({
      countryName: ['', Validators.required],
    });
  }

  submitCountryForm() {
    if (this.countryForm.invalid) return;

    const countryName = this.countryForm.value.countryName;
    this.apiService.getLeaguesByCountry(countryName).subscribe({
      next: (leagues) => {
        if (leagues && leagues.length > 0) {
          this.availableLeagues = leagues;
          this.showCountryForm = false;
          this.showLeagueList = true;
        } else {
          this.feedbackMessage = 'Brak lig dla podanego kraju.';
          this.showFeedback = true;
        }
      },
      error: (error) => {
        console.error('Error fetching leagues', error);
        this.feedbackMessage = 'Wystąpił błąd podczas pobierania lig.';
        this.showFeedback = true;
      },
    });
  }

  selectLeagueAndSubmit(league: any) {
    this.selectedLeague = league;
    console.log(this.selectedLeague);
    // const newLeagueData = { ...this.newLeagueForm.value, leagueId: league.id };
    // this.apiService.createNewLeague(newLeagueData).subscribe({
    //   next: (response) => {
    //     this.feedbackMessage = 'Nowa liga została pomyślnie utworzona!';
    //     this.showFeedback = true;
    //     this.showLeagueList = false;
    //     this.showNewLeagueForm = false;
    //   },
    //   error: (error) => {
    //     console.error('Error creating league', error);
    //     this.feedbackMessage = 'Wystąpił błąd podczas tworzenia ligi.';
    //     this.showFeedback = true;
    //   },
    // });
  }

  backToCountry(showCountryForm: boolean, showLeagueList: boolean) {
    this.showCountryForm = !showCountryForm;
    this.showLeagueList = !showLeagueList;
  }

  backToLeagues(showLeagueList: boolean, showTeamList: boolean) {
    this.showLeagueList = !showLeagueList;
    this.showTeamList = !showTeamList;
  }

  selectTeamAndSubmit(team: any) {}
}
