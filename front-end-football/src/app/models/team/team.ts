import { HomePageFixture } from '../home-page-fixture';

export interface Team {
  id: number;
  name: string;
}

export interface ApiMatches {
  fixtures: HomePageFixture[];
  emelents: number;
}
