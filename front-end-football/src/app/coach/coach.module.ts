import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoachRoutingModule } from './coach-routing.module';
import { PlayersComponent } from './players/players.component';
import { TeamComponent } from './team/team.component';
import { ProfileComponent } from './profile/profile.component';
import { TeamRadarChartComponent } from './team/team-radar-chart/team-radar-chart.component';
import { TeamChartComponent } from './team/team-chart/team-chart.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeModule } from '../home/home.module';
import { MatSortModule } from '@angular/material/sort';
import { NgApexchartsModule } from 'ng-apexcharts';
import { TeamDialogComponent } from './team/team-dialog/team-dialog.component';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { OponentComponent } from './oponent/oponent.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { StaffComponent } from './profile/staff/staff.component';
import { RequestsComponent } from './profile/requests/requests.component';
import { UserComponent } from './profile/user/user.component';
import { MatSelectModule } from '@angular/material/select';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    TeamChartComponent,
    TeamRadarChartComponent,
    PlayersComponent,
    TeamComponent,
    ProfileComponent,
    TeamChartComponent,
    TeamRadarChartComponent,
    TeamDialogComponent,
    OponentComponent,
    StaffComponent,
    RequestsComponent,
    UserComponent,
  ],
  imports: [
    CommonModule,
    CoachRoutingModule,
    ReactiveFormsModule,
    MatDialogModule,
    HomeModule,
    NgApexchartsModule,
    MatSortModule,
    ReactiveFormsModule,
    NgApexchartsModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatListModule,
    FormsModule,
    MatIconModule,
    MatSidenavModule,
    MatToolbarModule,
    MatSelectModule,
    MatExpansionModule,
    MatCheckboxModule,
    SharedModule,
  ],
  exports: [
    PlayersComponent,
    TeamComponent,
    ProfileComponent,
    TeamChartComponent,
    TeamRadarChartComponent,
  ],
})
export class CoachModule {}
