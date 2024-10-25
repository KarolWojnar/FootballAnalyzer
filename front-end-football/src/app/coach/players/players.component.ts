import { AfterViewInit, Component, OnDestroy, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { PlayerStats } from '../../models/players/player-stats';
import { ApiService } from '../../services/api.service';
import { PlayerService } from '../../services/players/player.service';

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.scss'],
})
export class PlayersComponent implements OnDestroy, AfterViewInit {
  form!: FormGroup;
  playerStats!: PlayerStats[];
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
    'penaltyWon',
    'penaltyCommitted',
  ];

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private playerService: PlayerService,
  ) {
    this.form = this.fb.group({ teamName: ['Arsenal'] });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
  }

  getPlayersData() {
    this.sub = this.apiService
      .fetchPlayerData(this.form.value.teamName)
      .subscribe({
        next: (player) => {
          this.playerStats = this.playerService.calcualtePlayers(player);
          setTimeout(() => {
            this.dataSource = new MatTableDataSource(this.playerStats);
            localStorage.setItem(
              'dataSource',
              JSON.stringify(this.playerStats),
            );
            this.dataSource.sort = this.sort;
            this.sub.unsubscribe();
          }, 500);
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
}
