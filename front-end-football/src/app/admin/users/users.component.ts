import {Component, OnInit} from '@angular/core';
import {UserAdmin} from "../../models/user.model";
import {ThemeService} from "../../services/theme.service";
import {ApiService} from "../../services/api.service";
import {Role} from "../../auth/components/register/register.component";
import {Team} from "../../models/team/team";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../../shared/components/confirm-dialog/confirm-dialog.component";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {RequestProblem} from "../../models/request/request";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss'],
})
export class UsersComponent implements OnInit{
  users: UserAdmin[] = [];
  isLoading: boolean = true;
  showMessage: boolean = false;
  message: string = '';
  request!: RequestProblem;
  requestInfo: { [key: string]: string | undefined };
  login: string;

  isDarkMode = true;
  objectKeys = Object.keys;
  roles: Role[] = [];
  teams: Team[] = [];
  userDataNew = new FormGroup({
    login: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),
    password: new FormControl(''),
    firstName: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),
    email: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),
    lastName: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),
  });
  constructor(private themeService: ThemeService,
              private apiService: ApiService,
              private dialog: MatDialog,) {
    this.themeService.darkMode$.subscribe((isDarkMode) => {
      this.isDarkMode = isDarkMode;
    });
    this.requestInfo = history.state.requestInfo;
    this.login = history.state.login;
  }

  ngOnInit(): void {
    this.fetchUsers();
    this.getTeams();
    this.getRoles();
  }

  downloadPdf(user: UserAdmin) {
    this.apiService.downloadPdf(user.id).subscribe((response: Blob) => {
      const blob = new Blob([response], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = user.login + '_potwierdzenie.pdf';
      link.click();
    });
  }

  private fetchUsers() {
    this.isLoading = true;
    this.apiService.getUsers().subscribe({
      next: (users: UserAdmin[]) => {
        this.users = users;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.showMessage = true;
        this.message = 'Problem z pobraniem użytkowników.';
      }
    });
  }

  deleteUser(user: UserAdmin) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      data: {
        "title": 'Potwierdzenie',
        "button": 'Usuń',
        "message": `Czy na pewno chcesz usunąć użytkownika ${user.login}?`,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.apiService.deleteUser(user.id).subscribe({
          next: () => {
            this.fetchUsers();
          },
          error: (error) => {
            this.showMessage = true;
            this.message = error.error.message;
          },
        });
      }
    });
  }

  sendQuery(user: UserAdmin) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      data: {
        "title": 'Potwierdzenie',
        "button": 'Wyślij',
        "message": `Czy na pewno chcesz wysłać zapytanie o potwierdzenie?`,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.request = {
          id: 0,
          login: user.login,
          requestStatus: 'NOWE',
          requestType: 'POTWIERDZENIE TOŻSAMOŚCI',
          requestData: {
            "message": "Proszę potweirdzić  swoją tożsamość w profilu użytkownika, inaczej twoja rola się zmieni.",
            "teamName": user.teamName
          },

        };
        this.apiService.addRequest(this.request).subscribe({
          next: () => {
            this.fetchUsers();
          },
          error: (error) => {
            this.showMessage = true;
            this.message = error.error.message;
          },
        });
      }
    });
  }

  changeUserRole(user: UserAdmin) {

  }

  changeTeam(user: UserAdmin) {
  }

  private getTeams() {
    this.apiService.getTeams().subscribe((teams: Team[]) => {
      this.teams = teams;
      this.teams.push({id: -1, name: 'Brak'} as Team)
    });
  }

  private getRoles() {
    this.apiService.getRolesAdmin().subscribe((roles: Role[]) => {
      this.roles = roles;
    });
  }

  changData(user: UserAdmin) {
    user.isEditing = !user.isEditing;
    this.userDataNew = new FormGroup({
      login: new FormControl(user.login, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      password: new FormControl(''),
      firstName: new FormControl(user.firstName, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      email: new FormControl(user.email, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      lastName: new FormControl(user.lastName, {
        validators: [Validators.required],
        nonNullable: true,
      }),
    });
  }

  setDataForUser(user: UserAdmin) {
    if (this.userDataNew.valid) {
      const data = this.userDataNew.value;
      user.login = data.login? data.login : user.login;
      user.firstName = data.firstName? data.firstName : user.firstName;
      user.lastName = data.lastName? data.lastName : user.lastName;
      user.email = data.email? data.email : user.email;
      user.password = data.password? data.password : user.password;
      this.changData(user);
    }
  }

  saveData(user: UserAdmin) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      data: {
        "title": 'Potwierdzenie',
        "button": 'Zapisz',
        "message": `Czy na pewno chcesz zapisać zmiany?`,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.apiService.updateUser(user).subscribe({
          next: () => {
            this.message = 'Zmiany zostały zapisane';
            this.showMessage = true;
            setTimeout(() => {
              this.fetchUsers();
              this.showMessage = false;
            }, 3000);
          },
          error: (error) => {
            this.showMessage = true;
            this.message = error.error.message;
          },
        });
      }
    });
  }
}
