import {
  AfterViewInit,
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { PlayerStats } from '../../models/players/player-stats';
import { ApiService } from '../../services/api.service';
import { PlayerService } from '../../services/players/player.service';
import { ThemeService } from '../../services/theme.service';
import { PlayerStatsForm } from '../../models/forms/forms.model';
import { FormService } from '../../services/form/form.service';
import { Stats, StatsPlayer } from '../../models/stats';
import { MatDialog } from '@angular/material/dialog';
import { ChartOptions } from '../../shared/components/team-stats/team-radar-chart/team-radar-chart.component';
import { PlayerRadarModalComponent } from './player-radar-modal/player-radar-modal.component';

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.scss'],
})
export class PlayersComponent implements OnDestroy, AfterViewInit, OnInit {
  form: FormGroup<PlayerStatsForm> = this.formService.initPlayerStatsForm();
  playerStats!: PlayerStats[];
  playerData!: StatsPlayer;
  dataSource: MatTableDataSource<PlayerStats> =
    new MatTableDataSource<PlayerStats>(
      JSON.parse(localStorage.getItem('dataSource')!) as PlayerStats[],
    );
  sub!: Subscription;
  @ViewChild(MatSort) sort!: MatSort;
  displayedColumns: string[] = [
    'player',
    'position',
    'minutes',
    'rating',
    'offsides',
    'shotsTotal',
    'shotsOnGoal',
    'goalsTotal',
    'goalsConceded',
    'assists',
    'saves',
    'passesTotal',
    'passesKey',
    'passesAccuracy',
    'tacklesTotal',
    'tacklesBlocks',
    'tacklesInterceptions',
    'duelsTotal',
    'duelsWon',
    'dribblesAttempts',
    'dribblesSuccess',
    'foulsDrawn',
    'foulsCommitted',
    'cardsYellow',
    'cardsRed',
  ];
  isDarkMode = true;
  formVisible: boolean = true;
  showAlert = false;
  chartOptions!: ChartOptions;
  alertMessage = '';
  isSubmitting = false;
  logoUrl = localStorage.getItem('logoUrl')!;
  teamStats: Stats = JSON.parse(localStorage.getItem('teamStats')!);

  constructor(
    private apiService: ApiService,
    private playerService: PlayerService,
    private formService: FormService,
    private themeService: ThemeService,
    private dialog: MatDialog,
  ) {
    this.themeService.darkMode$.subscribe((theme) => {
      this.isDarkMode = theme;
    });
  }

  ngOnInit(): void {
    if (localStorage.getItem('dataSource') == null) {
      this.getPlayersData();
    }
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  getPlayersData() {
    this.isSubmitting = true;
    this.sub = this.apiService.fetchPlayerData(this.form.value).subscribe({
      next: (player) => {
        setTimeout(() => {
          this.playerStats = this.playerService.calcualtePlayers(player);
          this.showAlert = false;
          this.isSubmitting = false;
          this.alertMessage = '';
          this.dataSource = new MatTableDataSource(this.playerStats);
          localStorage.setItem('dataSource', JSON.stringify(this.playerStats));
          localStorage.setItem(
            'startDatePlayer',
            JSON.stringify(this.form.value.startDate),
          );
          localStorage.setItem(
            'endDatePlayer',
            JSON.stringify(this.form.value.endDate),
          );
          setTimeout(() => {
            this.dataSource.sort = this.sort;
            this.sub.unsubscribe();
          }, 500);
        }, 500);
      },
      error: (error) => {
        this.isSubmitting = false;
        this.showAlert = true;
        if (error.error.status === 403) {
          this.alertMessage = 'Brak uprawnień do przeglądania zawodników.';
          return;
        }

        this.alertMessage = 'Brak danych o drużynie.';
      },
    });
  }

  onSubmit(): void {
    this.getPlayersData();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  toggleForm() {
    this.formVisible = !this.formVisible;
  }

  showChartRadar(player: any): void {
    this.apiService
      .getPlayerFromTeam(player.playerId, this.form.value)
      .subscribe({
        next: (result) => {
          this.playerData = result;
          this.initializeRadarChart();
        },
        error: (error) => {
          console.log(error);
        },
      });
  }

  initializeRadarChart() {
    this.chartOptions = {
      series: [
        {
          name: this.playerData.teamRating.team,
          data: [
            Math.round(this.playerData.teamRating.aggression * 100000) / 1000,
            Math.round(this.playerData.teamRating.attacking * 100000) / 1000,
            Math.round(this.playerData.teamRating.creativity * 100000) / 1000,
            Math.round(this.playerData.teamRating.defending * 100000) / 1000,
          ],
        },
        {
          name: this.playerData.allTeamsRating.team
            ? this.playerData.allTeamsRating.team
            : 'Średnia drużyny',
          data: [
            Math.round(this.playerData.allTeamsRating.aggression * 100000) /
              1000,
            Math.round(this.playerData.allTeamsRating.attacking * 100000) /
              1000,
            Math.round(this.playerData.allTeamsRating.creativity * 100000) /
              1000,
            Math.round(this.playerData.allTeamsRating.defending * 100000) /
              1000,
          ],
        },
      ],
      chart: {
        height: 500,
        type: 'radar',
        foreColor: this.isDarkMode ? '#f1f1f1' : '#2a2f3b',
        background: this.isDarkMode ? '#1e1e1e' : '#fff',
      },
      theme: {
        mode: this.isDarkMode ? 'dark' : 'light',
        palette: 'palette5',
      },
      stroke: {
        width: 1,
      },
      fill: {
        opacity: 0.6,
      },
      markers: {
        size: 3,
      },
      xaxis: {
        categories: ['Agresja', 'Atak', 'Kreatywność', 'Obrona'],
      },
      title: {
        text: 'Radar statystyk',
        style: {
          color: this.isDarkMode ? '#f1f1f1' : '#2a2f3b',
          fontSize: '20px',
        },
      },
    };
    console.log(this.chartOptions);
    this.dialog.open(PlayerRadarModalComponent, {
      width: '900px',
      data: {
        chartOptions: this.chartOptions as ChartOptions,
        isDarkMode: this.isDarkMode,
      },
    });
  }
}
