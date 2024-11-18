import { Injectable } from '@angular/core';
import { PlayerStats } from '../../models/players/player-stats';

@Injectable({
  providedIn: 'root',
})
export class PlayerService {
  constructor() {}

  calcualtePlayers(players: PlayerStats[]): PlayerStats[] {
    // todo: na przyszłość: dodać filtrowanie stat po określonej dacie

    const groupedPlayers = this.groupPlayersByName(players);

    return groupedPlayers.map((group) => this.aggregatePlayerStats(group));
  }

  private groupPlayersByName(players: PlayerStats[]): PlayerStats[][] {
    const playerGroups: { [key: string]: PlayerStats[] } = {};

    players.forEach((player) => {
      if (!playerGroups[player.player]) {
        playerGroups[player.player] = [];
      }
      playerGroups[player.player].push(player);
    });

    return Object.values(playerGroups);
  }

  private aggregatePlayerStats(playerGroup: PlayerStats[]): PlayerStats {
    const totalRecords = playerGroup.length;

    const aggregatedStats = playerGroup.reduce(
      (acc, curr) => ({
        date: undefined,
        player: curr.player,
        playerId: curr.playerId,
        position:
          curr.position === 'G'
            ? 'Bramkarz'
            : curr.position === 'D'
              ? 'Obrońca'
              : curr.position === 'M'
                ? 'Pomocnik'
                : 'Napastnik',
        minutes: acc.minutes + curr.minutes,
        rating: acc.rating + curr.rating,
        offsides: acc.offsides + curr.offsides,
        shotsTotal: acc.shotsTotal + curr.shotsTotal,
        shotsOnGoal: acc.shotsOnGoal + curr.shotsOnGoal,
        goalsTotal: acc.goalsTotal + curr.goalsTotal,
        goalsConceded: acc.goalsConceded + curr.goalsConceded,
        assists: acc.assists + curr.assists,
        saves: acc.saves + curr.saves,
        passesTotal: acc.passesTotal + curr.passesTotal,
        passesKey: acc.passesKey + curr.passesKey,
        passesAccuracy: acc.passesAccuracy + curr.passesAccuracy,
        tacklesTotal: acc.tacklesTotal + curr.tacklesTotal,
        tacklesBlocks: acc.tacklesBlocks + curr.tacklesBlocks,
        tacklesInterceptions:
          acc.tacklesInterceptions + curr.tacklesInterceptions,
        duelsTotal: acc.duelsTotal + curr.duelsTotal,
        duelsWon: acc.duelsWon + curr.duelsWon,
        dribblesAttempts: acc.dribblesAttempts + curr.dribblesAttempts,
        dribblesSuccess: acc.dribblesSuccess + curr.dribblesSuccess,
        foulsDrawn: acc.foulsDrawn + curr.foulsDrawn,
        foulsCommitted: acc.foulsCommitted + curr.foulsCommitted,
        cardsYellow: acc.cardsYellow + curr.cardsYellow,
        cardsRed: acc.cardsRed + curr.cardsRed,
        penaltyWon: acc.penaltyWon + curr.penaltyWon,
        penaltyCommitted: acc.penaltyCommitted + curr.penaltyCommitted,
      }),
      this.createEmptyPlayerStats(playerGroup[0].player),
    );
    aggregatedStats.rating = parseFloat(
      (aggregatedStats.rating / totalRecords).toFixed(2),
    );
    return aggregatedStats;
  }

  private createEmptyPlayerStats(playerName: string): PlayerStats {
    return {
      playerId: 0,
      date: undefined,
      position: '',
      player: playerName,
      minutes: 0,
      rating: 0,
      offsides: 0,
      shotsTotal: 0,
      shotsOnGoal: 0,
      goalsTotal: 0,
      goalsConceded: 0,
      assists: 0,
      saves: 0,
      passesTotal: 0,
      passesKey: 0,
      passesAccuracy: 0,
      tacklesTotal: 0,
      tacklesBlocks: 0,
      tacklesInterceptions: 0,
      duelsTotal: 0,
      duelsWon: 0,
      dribblesAttempts: 0,
      dribblesSuccess: 0,
      foulsDrawn: 0,
      foulsCommitted: 0,
      cardsYellow: 0,
      cardsRed: 0,
      penaltyWon: 0,
      penaltyCommitted: 0,
    };
  }
}
