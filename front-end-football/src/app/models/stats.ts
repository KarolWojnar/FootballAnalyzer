export class Stats {
  constructor(
    teamRating: TeamRating,
    allTeamsRating: TeamRating,
    teamForm: TeamForm,
    allTeamsForm: TeamForm,
  ) {
    this.teamRating = teamRating;
    this.allTeamsRating = allTeamsRating;
    this.teamForm = teamForm;
    this.allTeamsForm = allTeamsForm;
  }
  teamRating: TeamRating;
  allTeamsRating: TeamRating;
  teamForm: TeamForm;
  allTeamsForm: TeamForm;
}

interface TeamForm {
  team: string | null;
  rating: Record<string, number>;
}

interface TeamRating {
  team: string;
  date: string | null;
  attacking: number;
  defending: number;
  aggression: number;
  creativity: number;
}