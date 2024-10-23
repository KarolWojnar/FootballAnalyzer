export class PlayerStats {
  date!: Date | undefined;
  player: string;
  position: string;
  minutes: number;
  rating: number;
  offsides: number;
  shotsTotal: number;
  shotsOnGoal: number;
  goalsTotal: number;
  goalsConceded: number;
  assists: number;
  saves: number;
  passesTotal: number;
  passesKey: number;
  passesAccuracy: number;
  tacklesTotal: number;
  tacklesBlocks: number;

  constructor(
    date: Date,
    position: string,
    player: string,
    minutes: number,
    rating: number,
    offsides: number,
    shotsTotal: number,
    shotsOnGoal: number,
    goalsTotal: number,
    goalsConceded: number,
    assists: number,
    saves: number,
    passesTotal: number,
    passesKey: number,
    passesAccuracy: number,
    tacklesTotal: number,
    tacklesBlocks: number,
    tacklesInterceptions: number,
    duelsTotal: number,
    duelsWon: number,
    dribblesAttempts: number,
    dribblesSuccess: number,
    foulsDrawn: number,
    foulsCommitted: number,
    cardsYellow: number,
    cardsRed: number,
    penaltyWon: number,
    penaltyCommitted: number,
  ) {
    this.date = date;
    this.position = position;
    this.player = player;
    this.minutes = minutes;
    this.rating = rating;
    this.offsides = offsides;
    this.shotsTotal = shotsTotal;
    this.shotsOnGoal = shotsOnGoal;
    this.goalsTotal = goalsTotal;
    this.goalsConceded = goalsConceded;
    this.assists = assists;
    this.saves = saves;
    this.passesTotal = passesTotal;
    this.passesKey = passesKey;
    this.passesAccuracy = passesAccuracy;
    this.tacklesTotal = tacklesTotal;
    this.tacklesBlocks = tacklesBlocks;
    this.tacklesInterceptions = tacklesInterceptions;
    this.duelsTotal = duelsTotal;
    this.duelsWon = duelsWon;
    this.dribblesAttempts = dribblesAttempts;
    this.dribblesSuccess = dribblesSuccess;
    this.foulsDrawn = foulsDrawn;
    this.foulsCommitted = foulsCommitted;
    this.cardsYellow = cardsYellow;
    this.cardsRed = cardsRed;
    this.penaltyWon = penaltyWon;
    this.penaltyCommitted = penaltyCommitted;
  }

  tacklesInterceptions: number;
  duelsTotal: number;
  duelsWon: number;
  dribblesAttempts: number;
  dribblesSuccess: number;
  foulsDrawn: number;
  foulsCommitted: number;
  cardsYellow: number;
  cardsRed: number;
  penaltyWon: number;
  penaltyCommitted: number;
}
