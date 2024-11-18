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
  LoggedIn,
  ResetPassword,
  UserAdmin,
  UserLoginData,
  UserStaff,
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

  fetchTeamData(object: any): Observable<Stats> {
    const apiUrl = `${this.apiUrl}/coach/stats/team`;
    return this.httpClient.post<Stats>(apiUrl, object, {
      withCredentials: true,
    });
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

  fetchPlayerData(teamName: any): Observable<PlayerStats[]> {
    const requestUrl = `${this.apiUrl}/coach/stats/players`;
    return this.httpClient.post<PlayerStats[]>(requestUrl, teamName, {
      withCredentials: true,
    });
  }

  getTeams(): Observable<Team[]> {
    const requestUrl = `${this.apiUrl}/coach/all-teams`;
    return this.httpClient.get<Team[]>(requestUrl);
  }

  getTeamsFromLeague(leagueId: number): Observable<Team[]> {
    const requestUrl = `${this.apiUrl}/admin/clubsFromLeague/${leagueId}`;
    return this.httpClient.get<Team[]>(requestUrl, { withCredentials: true });
  }

  getNewLeague(
    leagueId: number | undefined,
    season: number | undefined,
  ): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/admin/fixtures/save-all-by-league-season/${leagueId}/${season}`;
    return this.httpClient.get<AuthResponse>(requestUrl, {
      withCredentials: true,
    });
  }

  getRoles(): Observable<Role[]> {
    const requestUrl = `${this.apiUrl}/coach/roles`;
    return this.httpClient.get<Role[]>(requestUrl);
  }

  getRolesAdmin(): Observable<Role[]> {
    const requestUrl = `${this.apiUrl}/users/roles`;
    return this.httpClient.get<Role[]>(requestUrl, { withCredentials: true });
  }

  downloadPdf(userId: number): Observable<Blob> {
    const requestUrl = `${this.apiUrl}/admin/requests/user/${userId}`;
    return this.httpClient.get(requestUrl, {
      responseType: 'blob',
      withCredentials: true,
    });
  }

  register(data: any): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/users/register`;
    return this.httpClient.post<AuthResponse>(requestUrl, data);
  }

  getUsers(): Observable<UserAdmin[]> {
    const requestUrl = `${this.apiUrl}/admin/users`;
    return this.httpClient.get<UserAdmin[]>(requestUrl, {
      withCredentials: true,
    });
  }

  uploadFile(login: string, file: File): Observable<any> {
    const requestUrl = `${this.apiUrl}/users/uploadConfirmFile/${login}`;
    let formData = new FormData();
    formData.append('file', file);
    return this.httpClient.patch<any>(requestUrl, formData);
  }
  deleteUser(userId: number): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/admin/users/${userId}`;
    return this.httpClient.delete<AuthResponse>(requestUrl, {
      withCredentials: true,
    });
  }

  updateUser(user: any): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/users/users/${user.id}`;
    return this.httpClient.patch<AuthResponse>(requestUrl, user, {
      withCredentials: true,
    });
  }

  addRequest(request: any) {
    const requestUrl = `${this.apiUrl}/users/requests`;
    return this.httpClient.post(requestUrl, request, { withCredentials: true });
  }

  getRequests(): Observable<RequestProblem[]> {
    const requestUrl = `${this.apiUrl}/admin/requests`;
    return this.httpClient.get<RequestProblem[]>(requestUrl, {
      withCredentials: true,
    });
  }

  getUserRequests(): Observable<RequestProblem[]> {
    const requestUrl = `${this.apiUrl}/coach/requests`;
    return this.httpClient.get<RequestProblem[]>(requestUrl, {
      withCredentials: true,
    });
  }

  deleteRequest(requestId: number) {
    const requestUrl = `${this.apiUrl}/admin/requests/${requestId}`;
    return this.httpClient.delete(requestUrl, { withCredentials: true });
  }

  updateRequest(request: RequestProblem) {
    const requestUrl = `${this.apiUrl}/coach/requests/${request.id}`;
    return this.httpClient.patch(requestUrl, request.requestStatus, {
      withCredentials: true,
    });
  }

  getLeagues() {
    const requestUrl = `${this.apiUrl}/coach/all-leagues`;
    return this.httpClient.get<League[]>(requestUrl);
  }

  login(body: UserLoginData): Observable<IUser> {
    const requestUrl = `${this.apiUrl}/users/login`;
    return this.httpClient.post<IUser>(requestUrl, body, {
      withCredentials: true,
    });
  }

  logout(): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/users/logout`;
    return this.httpClient.get<AuthResponse>(requestUrl, {
      withCredentials: true,
    });
  }

  autoLogin(): Observable<IUser> {
    const requestUrl = `${this.apiUrl}/users/auto-login`;
    return this.httpClient.get<IUser>(requestUrl, {
      withCredentials: true,
    });
  }

  isLoggedIn(): Observable<LoggedIn> {
    const requestUrl = `${this.apiUrl}/users/logged-in`;
    return this.httpClient.get<LoggedIn>(requestUrl, {
      withCredentials: true,
    });
  }

  getRole(): Observable<any> {
    const requestUrl = `${this.apiUrl}/users/role`;
    return this.httpClient.get<any>(requestUrl, {
      withCredentials: true,
    });
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
    const requestUrl = `${this.apiUrl}/users/reset-password`;
    return this.httpClient.patch<AuthResponse>(requestUrl, changePassword);
  }

  getLeaguesByCountry(countryName: string): Observable<League[]> {
    const requestUrl = `${this.apiUrl}/admin/leaguesFromCountry/${countryName}`;
    return this.httpClient.get<League[]>(requestUrl, { withCredentials: true });
  }

  getStaff(): Observable<UserStaff[]> {
    const requestUrl = `${this.apiUrl}/coach/staff`;
    return this.httpClient.get<UserStaff[]>(requestUrl, {
      withCredentials: true,
    });
  }

  removeFromTeam(id: number): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/coach/staff/${id}`;
    return this.httpClient.delete<AuthResponse>(requestUrl, {
      withCredentials: true,
    });
  }

  setAsCoach(id: number): Observable<AuthResponse> {
    const requestUrl = `${this.apiUrl}/coach/staff/${id}`;
    return this.httpClient.patch<AuthResponse>(
      requestUrl,
      {},
      {
        withCredentials: true,
      },
    );
  }

  getUserData(): Observable<UserAdmin> {
    const requestUrl = `${this.apiUrl}/coach/user`;
    return this.httpClient.get<UserAdmin>(requestUrl, {
      withCredentials: true,
    });
  }

  fetchTeamOpponent(object: any): Observable<Stats> {
    const apiUrl = `${this.apiUrl}/coach/stats/opponent`;
    return this.httpClient.post<Stats>(apiUrl, object, {
      withCredentials: true,
    });
  }
}
