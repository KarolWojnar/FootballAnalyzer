import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ApiService } from '../../../services/api.service';
import { Team } from '../../../models/team/team';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { FormService } from '../../../services/form/form.service';
import { RegisterForm } from '../../../models/forms/forms.model';
import { RequestProblem } from '../../../models/request/request';
import { ThemeService } from '../../../services/theme.service';
import { TeamDialogComponent } from '../../../shared/components/team-stats/team-dialog/team-dialog.component';

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
  document!: File;

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

  fileControl = new FormControl(null);

  onFileChange(event: any): void {
    const file = event.target.files[0];

    if (file) {
      if (file.type !== 'application/pdf') {
        this.alertMessage = 'Tylko pliki PDF są akceptowane.';
        this.showAlert = true;
        this.fileControl.setValue(null);
        return;
      }

      if (file.size > 5242880) {
        this.alertMessage = 'Plik jest za duży. Maksymalny rozmiar to 5MB.';
        this.showAlert = true;
        this.fileControl.setValue(null);
        return;
      }

      this.alertMessage = '';
      this.showAlert = false;
      this.document = file;
    }
  }

  getFileName(): string {
    const file = this.document;
    return file ? file.name : '';
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
      if (this.registerForm.value.checkBox?.valueOf() === true) {
        this.handleTakenTeam();
      }
      if (this.request?.requestType === 'Nowa drużyna') {
        this.registerForm.value.teamId = undefined;
      }

      this.apiService.register(this.registerForm.value).subscribe({
        next: () => {
          this.isSubmitting = false;
          this.alertMessage =
            'Konto zostało pomyślnie założone! Przekierowanie do logowania...';

          if (this.request) {
            this.request.login = this.registerForm.value.login;
            this.handleNewRequest(this.request);
            if (this.document) {
              if (this.registerForm.value.login) {
                const login = this.registerForm.value.login;
                this.apiService.uploadFile(login, this.document).subscribe({
                  next: () => {},
                  error: () => {},
                });
              }
            }
          }

          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error: ({ error }: { error: any }) => {
          this.coachTaken = false;
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
          id: 0,
          requestData: result,
          requestType: 'Nowa drużyna',
          requestStatus: 'NOWE',
          login: '',
          createdDate: null,
        };
      }
    });
  }

  private handleTakenTeam() {
    const teamId: number | undefined =
      this.registerForm.value.teamId?.valueOf();
    this.request = {
      id: 0,
      requestData: {
        drużyna:
          typeof teamId !== 'undefined'
            ? this.teams.find((t) => t.id === teamId)?.name
            : 'N/A',
        id: teamId ? teamId.toString() : 'N/A',
      },
      requestType: 'Trener zajęty',
      requestStatus: 'NOWE',
      login: '',
      createdDate: null,
    };
    this.registerForm.value.roleId = this.roles.find(
      (role) => role.roleName === 'ANALITYK',
    )?.id;
  }
}

export interface Role {
  roleName: string;
  id: number;
}
