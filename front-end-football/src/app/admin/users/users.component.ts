import {Component, OnInit} from '@angular/core';
import {UserAdmin} from "../../models/user.model";
import {ThemeService} from "../../services/theme.service";
import {ApiService} from "../../services/api.service";
import {Role} from "../../auth/components/register/register.component";
import {Team} from "../../models/team/team";

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
  requestInfo: { [key: string]: string | undefined };
  login: string;

  isDarkMode = true;
  objectKeys = Object.keys;
  roles: Role[] = [];
  teams: Team[] = [];
  constructor(private themeService: ThemeService,
              private apiService: ApiService) {
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

  }

  sendQuery(user: UserAdmin) {

  }

  changeUserRole(user: UserAdmin) {

  }

  changeTeam(user: UserAdmin) {

  }

  private getTeams() {
    this.apiService.getTeams().subscribe((teams: Team[]) => {
      this.teams = teams;
    });
  }

  private getRoles() {
    this.apiService.getRolesAdmin().subscribe((roles: Role[]) => {
      this.roles = roles;
    });
  }

  changData(user: UserAdmin) {

  }
}
