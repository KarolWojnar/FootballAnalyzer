import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ApiService } from '../../../services/api.service';
import { Team } from '../../../models/team/team';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TeamDialogComponent } from '../../../coach/team/team-dialog/team-dialog.component';
import { Request } from '../../../models/request/request';
import {v4} from "uuid";
import {FormService} from "../../../services/form/form.service";
import {RegisterForm} from "../../../models/forms/forms.model";

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
  login: string = v4()
  coachTaken = false;
  showAlert = false;
  alertMessage = '';
  isSubmitting = false;

  constructor(
    private apiService: ApiService,
    private router: Router,
    private dialog: MatDialog,
    private formService: FormService,
  ) {}
  registerForm:FormGroup<RegisterForm> = this.formService.initRegisterForm();

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
    return  this.formService.getErrorMessage(control);
  }

  handleNewRequest(request: Request) {
    this.apiService.addRequest(request).subscribe();
  }

  onRegister() {
    this.showAlert = this.registerForm.invalid;
    this.alertMessage = 'Formularz zawiera błędy';
    console.log(this.registerForm.value)
    console.log(this.controls)

    if (this.registerForm.valid) {
      this.isSubmitting = true;
      console.log(this.registerForm.value);
      this.apiService.register(this.registerForm.value).subscribe({
        next: (next) => {
          this.isSubmitting = false;
          this.alertMessage = 'Konto zostało pomyślnie założone! Przekierowanie do logowania...';

          if (this.request) {
            this.request.login = next.login;
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
