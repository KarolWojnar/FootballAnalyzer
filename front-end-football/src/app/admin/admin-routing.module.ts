import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { AdminGuard } from '../guards/admin.guard';
import { UsersComponent } from './users/users.component';
import { RequestsComponent } from './requests/requests.component';
import { TeamsComponent } from './teams/teams.component';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuard],
    children: [
      { path: 'users', component: UsersComponent, title: 'Users' },
      { path: 'requests', component: RequestsComponent, title: 'Requests' },
      { path: 'teams', component: TeamsComponent, title: 'Teams' },
      { path: '', redirectTo: 'users', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
