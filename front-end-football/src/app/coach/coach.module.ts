import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoachRoutingModule } from './coach-routing.module';
import { PlayersComponent } from './players/players.component';
import { TeamComponent } from './team/team.component';
import { ProfileComponent } from './profile/profile.component';
import { TeamRadarChartComponent } from './team/team-radar-chart/team-radar-chart.component';
import { TeamChartComponent } from './team/team-chart/team-chart.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HomeModule } from '../home/home.module';
import { MatSortModule } from '@angular/material/sort';
import { NgApexchartsModule } from 'ng-apexcharts';

@NgModule({
  declarations: [
    TeamChartComponent,
    TeamRadarChartComponent,
    PlayersComponent,
    TeamComponent,
    ProfileComponent,
    TeamChartComponent,
    TeamRadarChartComponent,
  ],
  imports: [
    CommonModule,
    CoachRoutingModule,
    ReactiveFormsModule,
    HomeModule,
    NgApexchartsModule,
    MatSortModule,
    ReactiveFormsModule,
    NgApexchartsModule,
  ],
  exports: [PlayersComponent, TeamComponent, ProfileComponent],
})
export class CoachModule {}
