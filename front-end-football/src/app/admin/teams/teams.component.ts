import {Component} from '@angular/core';
import {ThemeService} from '../../services/theme.service';
import {FormService} from '../../services/form/form.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NewLeagueForm} from '../../models/forms/forms.model';
import {ApiService} from '../../services/api.service';
import {Router} from '@angular/router';
import {League} from "../../models/league";
import {Team} from "../../models/team/team";
import {TranslatorService} from "../../services/translator.service";

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
  requestId: number;
  isSubmitting = false;
  feedbackLeagueMessage = '';
  showFeedbackLeague = false;

  availableLeagues: League[] = [];
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
    private translatorService: TranslatorService
  ) {
    this.themeService.darkMode$.subscribe((isDarkMode) => {
      this.isDarkMode = isDarkMode;
    });
    this.teamInfo = history.state.teamInfo;
    this.requestId = history.state.id;
  }

  initCountryForm(): FormGroup {
    return this.fb.group({
      countryName: ['', Validators.required],
    });
  }

  submitCountryForm() {
    if (this.countryForm.invalid) return;
    this.showFeedback = false;
    const countryName = this.translatorService.translateCountryName(this.countryForm.value.countryName);
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

  selectLeagueAndSubmit(league: League) {
    this.selectedLeague = league.leagueId;
    if (this.selectedLeague) {
      this.apiService.getTeamsFromLeague(this.selectedLeague).subscribe({
        next: (teams) => {
          if (teams && teams.length > 0) {
            this.availableTeams = teams;
            this.showLeagueList = false;
            this.showTeamList = true;
          } else {
            this.feedbackMessage = 'Brak lig dla podanego kraju.';
            this.showFeedback = true;
          }
        },
        error: (error) => {
          console.error('Error fetching teams', error);
          this.feedbackMessage = 'Wystąpił błąd podczas pobierania lig.';
          this.showFeedback = true;
        },
      });
    }
  }

  backToCountry(showCountryForm: boolean, showLeagueList: boolean) {
    this.showCountryForm = !showCountryForm;
    this.showLeagueList = !showLeagueList;
  }

  backToLeagues(showLeagueList: boolean, showTeamList: boolean) {
    this.showLeagueList = !showLeagueList;
    this.showTeamList = !showTeamList;
  }

  selectTeamAndSubmit(team: Team) {
    const league = this.availableLeagues.find((league) => league.leagueId === this.selectedLeague);
    this.feedbackMessage = team.name + ' gra w lidze: ' + league?.name +
    '.    ID ligi: ' + league?.leagueId.toString();
    this.showFeedback = true;
    this.showTeamList = false;
  }

  submitNewLeague() {
    if (this.newLeagueForm.invalid) return;
    this.isSubmitting = true;
    this.showFeedbackLeague = false;
    const {leagueId, season} = this.newLeagueForm.value;
    this.apiService.getNewLeague(leagueId, season).subscribe({
      next: (response) => {
        this.showNewLeagueForm = false;
        this.feedbackLeagueMessage = response.message;
        this.showFeedbackLeague = true;
        this.isSubmitting = false;
      },
      error: (error) => {
        console.error('Error fetching teams', error);
        this.feedbackLeagueMessage = error.error.message;
        this.showFeedbackLeague = true;
        this.isSubmitting = false;
      },
    });
  }
}
