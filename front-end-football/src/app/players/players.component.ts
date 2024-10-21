import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ApiService } from '../services/api.service';
import { PlayerStats } from '../models/players/player-stats';
import { Subscription } from 'rxjs';
import { PlayerService } from "../services/players/player.service";
import { MatTableDataSource } from "@angular/material/table";

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.scss'],
})
export class PlayersComponent implements OnDestroy{
  form!: FormGroup;
  playerStats!: PlayerStats[];
  dataSource!: MatTableDataSource<PlayerStats>;
  sub!: Subscription;
  displayedColumns: string[] = [
    'player', 'position', 'minutes', 'rating', 'offsides', 'shotsTotal',
    'shotsOnGoal', 'goalsTotal', 'goalsConceded', 'assists', 'saves',
    'passesTotal', 'passesKey', 'passesAccuracy', 'tacklesTotal',
    'tacklesBlocks', 'tacklesInterceptions', 'duelsTotal', 'duelsWon',
    'dribblesAttempts', 'dribblesSuccess', 'foulsDrawn', 'foulsCommitted',
    'cardsYellow', 'cardsRed', 'penaltyWon', 'penaltyCommitted'
  ];

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private playerService: PlayerService,
  ) {
    this.form = this.fb.group({ teamName: ['Arsenal'] });
  }

  getPlayersData() {
    this.sub = this.apiService.fetchPlayerData(this.form.value.teamName).subscribe({
        next: (player) => {
          this.playerStats = this.playerService.calcualtePlayers(player);
          setTimeout(() => {
            this.dataSource = new MatTableDataSource(this.playerStats);
            this.sub.unsubscribe();
          }, 1500);
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
