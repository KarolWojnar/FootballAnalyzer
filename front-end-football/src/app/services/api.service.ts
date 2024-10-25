import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HomePageFixture } from '../models/home-page-fixture';
import { Stats } from '../models/stats';
import { PlayerStats } from '../models/players/player-stats';
import { environment } from '../../environments/environment.development';

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
    const apiUrl = `${this.apiUrl}/api/coach/stats/team?teamName=${teamName}&startDate=${startDate}&endDate=${endDate}&rounding=${rounding}`;
    return this.httpClient.get<Stats>(apiUrl);
  }

  getMatches(today: Date, page: number): Observable<ApiMatches> {
    const requestUrl = `${this.apiUrl}/api/coach/futureMatches?startDate=${today.toISOString().split('T')[0]}&page=${page}`;
    return this.httpClient.get<ApiMatches>(requestUrl);
  }

  fetchPlayerData(teamName: string): Observable<PlayerStats[]> {
    const requestUrl = `${this.apiUrl}/api/coach/stats/players?teamName=${teamName}`;
    return this.httpClient.get<PlayerStats[]>(requestUrl);
  }
}

export interface ApiMatches {
  fixtures: HomePageFixture[];
  emelents: number;
}
