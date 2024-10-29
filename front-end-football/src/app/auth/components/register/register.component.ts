import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../../../services/api.service';
import { Team } from '../../../models/team/team';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TeamDialogComponent } from '../../../coach/team/team-dialog/team-dialog.component';
import { Request } from '../../../models/request/request';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  hide = true;
  request: Request | undefined;
  teams: Team[] = [];
  roles: Role[] = [];
  showAlert = false;
  alertMessage = '';

  constructor(
    private apiService: ApiService,
    private router: Router,
    private dialog: MatDialog,
  ) {}
  registerForm = new FormGroup(
    {
      firstName: new FormControl('sdadasd', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      lastName: new FormControl('sdadasd', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      email: new FormControl('sdadasd@wp.pl', {
        validators: [Validators.required, Validators.email],
        nonNullable: true,
      }),
      password: new FormControl('sdadasd', {
        validators: [Validators.required, Validators.minLength(8)],
        nonNullable: true,
      }),
      login: new FormControl('sdadasd', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      teamId: new FormControl('', {
        validators: [],
      }),
      roleId: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
    },
    { updateOn: 'submit' },
  );

  get controls() {
    return this.registerForm.controls;
  }

  ngOnInit(): void {
    this.controls;
    this.getTeams();
    this.getRoles();
  }

  getErrorMessage(control: FormControl) {
    if (control?.hasError('required')) {
      return 'Pole nie może byc puste';
    } else if (control?.hasError('minlength')) {
      return 'Hasło musi mieć co najmniej 8 znaków';
    } else if (control?.hasError('email')) {
      return 'Nieprawidłowy adres email';
    }
    return control?.hasError('') ? 'Not a valid email' : '';
  }

  handleNewRequest(request: Request) {
    this.apiService.addRequest(request).subscribe();
  }

  onRegister() {
    this.showAlert = this.registerForm.invalid;
    this.alertMessage = 'Formularz zawiera błędy';
    if (this.registerForm.valid) {
      this.apiService.register(this.registerForm.value).subscribe({
        next: (next) => {
          if (this.request) {
            this.request.login = next.login;
            this.handleNewRequest(this.request);
          }
          this.router.navigate(['/login']).then();
        },
        error: ({ error }: { error: any }) => {
          this.alertMessage = error;
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
          requestStatus: 'PENDING',
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
