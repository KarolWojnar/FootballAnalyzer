export class League {
  constructor(leagueId: number, name: string, logo: string, selected: boolean) {
    this.leagueId = leagueId;
    this.name = name;
    this.logo = logo;
    this.selected = selected;
  }
    leagueId: number;
    name: string;
    logo: string;
    selected: boolean = false;
}
