import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ApiService } from '../services/api.service';
import { PlayerStats } from '../models/players/player-stats';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.scss'],
})
export class PlayersComponent {
  form!: FormGroup;
  playerStats!: PlayerStats[];
  sub!: Subscription;

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
  ) {
    this.form = this.fb.group({ teamName: [''] });
  }

  getPlayersData() {
    this.sub = this.apiService
      .fetchPlayerData(this.form.value.teamName)
      .subscribe({
        next: (player) => {
          this.playerStats = player;
          console.log(this.playerStats);
        },
      });
  }

  onSubmit(): void {
    this.getPlayersData();
  }
}
