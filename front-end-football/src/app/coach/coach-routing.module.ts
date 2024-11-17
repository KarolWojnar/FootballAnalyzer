import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PlayersComponent } from './players/players.component';
import { TeamComponent } from './team/team.component';
import { ProfileComponent } from './profile/profile.component';
import { OponentComponent } from './oponent/oponent.component';
import { AuthGuard } from '../guards/auth.guard';
import { CoachGuard } from '../guards/coach.guard';
import { StaffComponent } from './profile/staff/staff.component';
import { UserComponent } from './profile/user/user.component';
import { RequestsComponent } from './profile/requests/requests.component';

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
    children: [
      { path: 'user', component: UserComponent, title: 'Profile' },
      { path: 'requests', component: RequestsComponent, title: 'Requestes' },
      {
        path: 'staff',
        component: StaffComponent,
        title: 'Staff',
        canActivate: [CoachGuard],
      },
      { path: '', redirectTo: 'users', pathMatch: 'full' },
    ],
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
