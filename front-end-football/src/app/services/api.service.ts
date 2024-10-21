import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HomePageFixture } from '../models/home-page-fixture';
import { Stats } from '../models/stats';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private httpClient: HttpClient) {}

  fetchTeamData(
    teamName: string,
    startDate: string,
    endDate: string,
    rounding: string,
  ): Observable<Stats> {
    const href = 'http://localhost:8080/api/coach/stats/';
    const apiUrl = `${href}team?teamName=${teamName}&startDate=${startDate}&endDate=${endDate}&rounding=${rounding}`;
    return this.httpClient.get<Stats>(apiUrl);
  }

  getMatches(today: Date, page: number): Observable<HomePageFixture[]> {
    const href = 'http://localhost:8080/api/coach/futureMatches';
    const requestUrl = `${href}?startDate=${today.toISOString().split('T')[0]}&page=${page + 1}`;
    return this.httpClient.get<HomePageFixture[]>(requestUrl);
  }
}
