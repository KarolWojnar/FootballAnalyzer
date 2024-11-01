export class HomePageFixture {
  constructor(awayTeam: string, homeTeam: string, matchDate: string, logoHome: string, logoAway: string, leagueId: number) {
    this.awayTeam = awayTeam;
    this.homeTeam = homeTeam;
    this.matchDate = matchDate;
    this.logoHome = logoHome;
    this.logoAway = logoAway;
    this.leagueId = leagueId;
  }
  awayTeam: string;
  homeTeam: string;
  matchDate: string;
  logoHome: string;
  logoAway: string;
  leagueId: number;
}
