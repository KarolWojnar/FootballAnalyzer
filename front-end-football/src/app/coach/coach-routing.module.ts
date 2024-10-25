import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PlayersComponent } from './players/players.component';
import { TeamComponent } from './team/team.component';
import { ProfileComponent } from './profile/profile.component';

const routes: Routes = [
  { path: 'players', component: PlayersComponent, title: 'Players' },
  { path: 'team', component: TeamComponent, title: 'Team' },
  { path: 'profile', component: ProfileComponent, title: 'Profile' },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CoachRoutingModule {}
