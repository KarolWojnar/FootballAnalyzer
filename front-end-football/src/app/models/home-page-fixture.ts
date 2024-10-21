export class HomePageFixture {
  constructor(awayTeam: string, homeTeam: string, matchDate: string) {
    this.awayTeam = awayTeam;
    this.homeTeam = homeTeam;
    this.matchDate = matchDate;
  }
  awayTeam: string;
  homeTeam: string;
  matchDate: string;
}
