import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Stats } from '../models/stats';
import { PlayerStats } from '../models/players/player-stats';
import { environment } from '../../environments/environment.development';
import { ApiMatches, Team } from '../models/team/team';
import {
  AuthResponse,
  ChangePassword,
  IUser,
  ResetPassword,
  UserLoginData,
} from '../models/user.model';
import { Role } from '../auth/components/register/register.component';
import { League } from '../models/league';
import { RequestProblem } from '../models/request/request';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = environment.apiUrl;

  constructor(private httpClient: HttpClient) {}

  fetchTeamData(
    teamName: string,
    startDate: string,
    endDate: string,
    rounding: string,
  ): Observable<Stats> {
    const apiUrl = `${this.apiUrl}/coach/stats/team?teamName=${teamName}&startDate=${startDate}&endDate=${endDate}&rounding=${rounding}`;
    return this.httpClient.get<Stats>(apiUrl);
  }

  getMatches(
    today: Date,
    page: number,
    leagueId: number,
  ): Observable<ApiMatches> {
    let requestUrl = `${this.apiUrl}/coach/futureMatches?startDate=${today.toISOString().split('T')[0]}&page=${page}`;
    if (leagueId != null) {
      requestUrl = `${this.apiUrl}/coach/futureMatches?startDate=${today.toISOString().split('T')[0]}&page=${page}&leagueId=${leagueId}`;
    }
    return this.httpClient.get<ApiMatches>(requestUrl);
  }

  fetchPlayerData(teamName: string): Observable<PlayerStats[]> {
    const requestUrl = `${this.apiUrl}/coach/stats/players?teamName=${teamName}`;
    return this.httpClient.get<PlayerStats[]>(requestUrl);
  }

  getTeams(): Observable<Team[]> {
    const requestUrl = `${this.apiUrl}/coach/all-teams`;
    return this.httpClient.get<Team[]>(requestUrl);
  }

  getRoles(): Observable<Role[]> {
    const requestUrl = `${this.apiUrl}/coach/roles`;
    return this.httpClient.get<Role[]>(requestUrl);
  }

  register(data: any): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/users/register`;
    return this.httpClient.post<AuthResponse>(requestUrl, data);
  }

  addRequest(request: RequestProblem) {
    const requestUrl = `${this.apiUrl}/users/requests`;
    return this.httpClient.post(requestUrl, request);
  }

  getLeagues() {
    const requestUrl = `${this.apiUrl}/coach/all-leagues`;
    return this.httpClient.get<League[]>(requestUrl);
  }

  login(body: UserLoginData): Observable<IUser> {
    const requestUrl = `${this.apiUrl}/users/login`;
    return this.httpClient.post<IUser>(requestUrl, body);
  }

  logout(): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/users/logout`;
    return this.httpClient.get<AuthResponse>(requestUrl);
  }

  activateAccount(uuid: string): Observable<AuthResponse> {
    const params = new HttpParams().append('uuid', uuid);
    const requestUrl = `${this.apiUrl}/users/activate`;
    return this.httpClient.get<AuthResponse>(requestUrl, {
      params: params,
    });
  }

  resetPassword(email: ResetPassword): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/users/reset-password`;
    return this.httpClient.post<AuthResponse>(requestUrl, email);
  }

  changePassword(changePassword: ChangePassword): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/users//reset-password`;
    return this.httpClient.patch<AuthResponse>(requestUrl, changePassword);
  }
}
