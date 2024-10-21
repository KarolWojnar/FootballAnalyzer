import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HomePageFixture } from '../models/home-page-fixture';
import { Stats } from '../models/stats';
import { PlayerStats } from '../models/players/player-stats';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private href = 'http://localhost:8080/api/';

  constructor(private httpClient: HttpClient) {}

  fetchTeamData(
    teamName: string,
    startDate: string,
    endDate: string,
    rounding: string,
  ): Observable<Stats> {
    const apiUrl = `${this.href}coach/stats/team?teamName=${teamName}&startDate=${startDate}&endDate=${endDate}&rounding=${rounding}`;
    return this.httpClient.get<Stats>(apiUrl);
  }

  getMatches(today: Date, page: number): Observable<HomePageFixture[]> {
    const requestUrl = `${this.href}coach/futureMatches?startDate=${today.toISOString().split('T')[0]}&page=${page + 1}`;
    return this.httpClient.get<HomePageFixture[]>(requestUrl);
  }

  fetchPlayerData(teamName: string): Observable<PlayerStats[]> {
    const requestUrl = `${this.href}coach/stats/players?teamName=${teamName}`;
    return this.httpClient.get<PlayerStats[]>(requestUrl);
  }
}
