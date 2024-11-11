import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PlayersComponent } from './players/players.component';
import { TeamComponent } from './team/team.component';
import { ProfileComponent } from './profile/profile.component';
import { OponentComponent } from './oponent/oponent.component';
import { AuthGuard } from '../guards/auth.guard';
import { CoachGuard } from '../guards/coach.guard';

const routes: Routes = [
  {
    path: 'players',
    component: PlayersComponent,
    title: 'Players',
    canActivate: [AuthGuard],
  },
  {
    path: 'team',
    component: TeamComponent,
    title: 'Team',
    canActivate: [AuthGuard],
  },
  {
    path: 'profile',
    component: ProfileComponent,
    title: 'Profile',
    canActivate: [AuthGuard],
  },
  {
    path: 'opponent',
    component: OponentComponent,
    title: 'Opponent',
    canActivate: [AuthGuard, CoachGuard],
  },
  { path: '**', redirectTo: 'home', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CoachRoutingModule {}
