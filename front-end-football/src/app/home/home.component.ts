import {
  AfterViewInit,
  Component,
  HostListener,
  OnInit,
  ViewChild,
} from '@angular/core';
import { HomePageFixture } from '../models/home-page-fixture';
import { MatPaginator } from '@angular/material/paginator';
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  map,
  merge,
  of,
  startWith,
  Subscription,
  switchMap,
} from 'rxjs';
import { ApiService } from '../services/api.service';
import { League } from '../models/league';
import { ThemeService } from '../services/theme.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements AfterViewInit, OnInit {
  isDarkMode = false;
  today: Date = new Date();

  leagueId!: number;
  sub = new Subscription();
  data: HomePageFixture[] = [];
  leagues: League[] = [];
  resultsLength = 0;
  isLoadingResults = false;
  isRateLimitReached = false;
  isSmallScreen = false;
  isExtraSmallScreen = false;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private apiService: ApiService,
    private themeService: ThemeService,
  ) {
    this.updateScreenSize();
  }

  ngOnInit(): void {
    this.themeService.darkMode$.subscribe((isDark) => {
      this.isDarkMode = isDark;
    });
  }

  displayedColumns: string[] = ['matchDate', 'homeTeam', 'awayTeam'];
  filterValue = new FormControl('', { nonNullable: true });

  ngAfterViewInit(): void {
    this.getLeagues();
    this.getMatches();
  }

  getLeagues() {
    this.apiService.getLeagues().subscribe((leagues) => {
      this.leagues = leagues;
    });
    this.leagues.forEach((l) => {
      l.selected = false;
    });
  }

  selectLeague(league: League) {
    this.leagues.forEach((l) => {
      l.selected = false;
    });
    league.selected = true;
    this.leagueId = league.leagueId;
    this.paginator.pageIndex = 0;
    this.filterValue.setValue('');
    this.getMatches();
  }

  private getMatches() {
    this.sub.add(
      merge(
        this.paginator.page,
        this.filterValue.valueChanges.pipe(
          debounceTime(500),
          distinctUntilChanged(),
        ),
      )
        .pipe(
          startWith({}),
          switchMap(() => {
            return this.apiService
              .getMatches(
                this.today,
                this.paginator.pageIndex,
                this.leagueId,
                this.filterValue.value.trim(),
              )
              .pipe(catchError(() => of(null)));
          }),
          map((data) => {
            this.isRateLimitReached = data === null;

            if (data === null) {
              return [];
            }

            this.resultsLength = data.emelents || 0;
            return data.fixtures;
          }),
        )
        .subscribe((data) => (this.data = data)),
    );
  }

  @HostListener('window:resize', [])
  updateScreenSize() {
    this.isSmallScreen = window.innerWidth <= 768;
    this.isExtraSmallScreen = window.innerWidth <= 576;
  }

  getDate(date: any) {
    return new Date(date).toLocaleDateString('pl-PL', {
      weekday: 'long',
    });
  }

  applyFilter(event: string) {
    this.filterValue.setValue(event.trim());
  }
}
