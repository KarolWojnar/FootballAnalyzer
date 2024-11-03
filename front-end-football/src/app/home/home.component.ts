import {AfterViewInit, Component, HostListener, ViewChild} from '@angular/core';
import { HomePageFixture } from '../models/home-page-fixture';
import { MatPaginator } from '@angular/material/paginator';
import { catchError, map, merge, of, startWith, switchMap } from 'rxjs';
import { ApiService } from '../services/api.service';
import {League} from "../models/league";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements AfterViewInit {
  constructor(private apiService: ApiService) {
    this.updateScreenSize();
  }

  displayedColumns: string[] = ['matchDate', 'homeTeam', 'awayTeam'];

  today: Date = new Date();
  leagueId!: number;
  data: HomePageFixture[] = [];
  leagues: League[] = [];
  resultsLength = 0;
  isLoadingResults = true;
  isRateLimitReached = false;
  isSmallScreen = false;
  isExtraSmallScreen = false;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngAfterViewInit(): void {
    this.getLeagues();
    this.getMatches()
  }

  getLeagues() {
    this.apiService.getLeagues().subscribe((leagues) => {
      this.leagues = leagues;
    });
    this.leagues.forEach(
      (l) => {
        l.selected = false;
      });
  }

  selectLeague(league: League){
    this.leagues.forEach(
      (l) => {
      l.selected = false;
    });
    league.selected = true;
    this.leagueId = league.leagueId;
    this.paginator.pageIndex = 0;
    this.getMatches();
  }

  private getMatches() {
    merge(this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.apiService
            .getMatches(this.today, this.paginator.pageIndex, this.leagueId)
            .pipe(catchError(() => of(null)));
        }),
        map((data) => {
          this.isLoadingResults = false;
          this.isRateLimitReached = data === null;

          if (data === null) {
            return [];
          }

          this.paginator._intl.itemsPerPageLabel = 'Mecze na stronie:';
          this.paginator._intl.nextPageLabel = 'Następna strona';
          this.paginator._intl.previousPageLabel = 'Poprzednia strona';
          this.paginator._intl.lastPageLabel = 'Ostatnia strona';
          this.paginator._intl.firstPageLabel = 'Pierwsza strona';
          this.paginator._intl.getRangeLabel = (page, pageSize, length) => {
            return `Strona ${page + 1} z ${Math.ceil(length / pageSize)}`;
          };

          this.resultsLength = data.emelents;
          return data.fixtures;
        }),
      )
      .subscribe((data) => (this.data = data));
  }

  @HostListener('window:resize', [])
  updateScreenSize() {
    this.isSmallScreen = window.innerWidth <= 768;
    this.isExtraSmallScreen = window.innerWidth <= 576;
  }
}
