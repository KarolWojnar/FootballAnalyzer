import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {HomePageFixture} from '../models/home-page-fixture';
import {Stats} from '../models/stats';
import {PlayerStats} from '../models/players/player-stats';
import {environment} from '../../environments/environment.development';
import {Team} from '../models/team/team';
import {UserResponse} from '../models/user.model';
import {Role} from '../auth/components/register/register.component';
import {Request} from '../models/request/request';
import {League} from "../models/league";

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

  getMatches(today: Date, page: number, leagueId: number): Observable<ApiMatches> {
    let requestUrl = `${this.apiUrl}/coach/futureMatches?startDate=${today.toISOString().split('T')[0]}&page=${page}`;
    if (leagueId != null) {
      requestUrl = `${this.apiUrl}/coach/futureMatches?startDate=${today.toISOString().split('T')[0]}&page=${page}&leagueId=${leagueId}`;
    }
    console.log(requestUrl);
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

  register(data: any): Observable<UserResponse> {
    const requestUrl = `${this.apiUrl}/users/register`;
    return this.httpClient.post<UserResponse>(requestUrl, data);
  }

  addRequest(request: Request) {
    const requestUrl = `${this.apiUrl}/users/requests`;
    return this.httpClient.post(requestUrl, request);
  }

  getLeagues() {
    const requestUrl = `${this.apiUrl}/coach/all-leagues`;
    return this.httpClient.get<League[]>(requestUrl);
  }
}

export interface ApiMatches {
  fixtures: HomePageFixture[];
  emelents: number;
}
