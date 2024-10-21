import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PlayersComponent } from './players/players.component';
import { TeamComponent } from './team/team.component';
import { AdminComponent } from './admin/admin.component';
import { ProfileComponent } from './profile/profile.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full', title: 'Home' },
  { path: 'players', component: PlayersComponent, title: 'Players' },
  { path: 'team', component: TeamComponent, title: 'Team' },
  { path: 'admin', component: AdminComponent, title: 'Admin' },
  { path: 'profile', component: ProfileComponent, title: 'Profile' },
  { path: '**', component: PageNotFoundComponent, title: 'Page Not Found' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
