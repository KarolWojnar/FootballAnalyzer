import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ApiService } from '../../../services/api.service';
import { Team } from '../../../models/team/team';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TeamDialogComponent } from '../../../coach/team/team-dialog/team-dialog.component';
import { FormService } from '../../../services/form/form.service';
import { RegisterForm } from '../../../models/forms/forms.model';
import { RequestProblem } from '../../../models/request/request';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  hide = true;
  request: RequestProblem | undefined;
  teams: Team[] = [];
  roles: Role[] = [];
  coachTaken = false;
  showAlert = false;
  alertMessage = '';
  isSubmitting = false;
  isDarkMode = true;

  constructor(
    private apiService: ApiService,
    private router: Router,
    private dialog: MatDialog,
    private formService: FormService,
    private themeService: ThemeService,
  ) {
    this.themeService.darkMode$.subscribe((isDark) => {
      this.isDarkMode = isDark;
    });
  }

  registerForm: FormGroup<RegisterForm> = this.formService.initRegisterForm();

  get controls() {
    return this.registerForm.controls;
  }

  ngOnInit(): void {
    this.controls;
    this.getTeams();
    this.getRoles();
    this.coachTaken = false;
  }

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  handleNewRequest(request: RequestProblem) {
    this.apiService.addRequest(request).subscribe();
  }

  onRegister() {
    this.showAlert = this.registerForm.invalid;
    this.alertMessage = 'Formularz zawiera błędy';

    if (this.registerForm.valid) {
      this.isSubmitting = true;
      this.apiService.register(this.registerForm.value).subscribe({
        next: () => {
          this.isSubmitting = false;
          this.alertMessage =
            'Konto zostało pomyślnie założone! Przekierowanie do logowania...';

          if (this.request) {
            this.request.login = this.registerForm.value.login;
            this.handleNewRequest(this.request);
          }

          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error: ({ error }: { error: any }) => {
          this.isSubmitting = false;
          this.alertMessage = error.message;

          if (error.code === 'R2') {
            this.coachTaken = true;
          }
          this.showAlert = true;
        },
      });
    }
  }

  getRoles() {
    this.apiService.getRoles().subscribe((roles) => {
      this.roles = roles;
    });
  }

  getTeams() {
    this.apiService.getTeams().subscribe((teams) => {
      this.teams = teams;
    });
  }

  openTeamDialog() {
    const dialogRef = this.dialog.open(TeamDialogComponent);
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.request = {
          requestData: result,
          requestType: 'ADD_TEAM',
          requestStatus: 'NOWE',
          login: '',
        };
      }
    });
  }
}

export interface Role {
  roleName: string;
  id: number;
}
