import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertComponent } from './components/alert/alert.component';
import { HeaderComponent } from './header/header.component';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { RequestProblemDialogComponent } from './request-problem-dialog/request-problem-dialog.component';
import { MatInputModule } from '@angular/material/input';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { ReactiveFormsModule } from '@angular/forms';
import { TeamStatsComponent } from './components/team-stats/team-stats.component';
import { TeamChartComponent } from './components/team-stats/team-chart/team-chart.component';
import { TeamRadarChartComponent } from './components/team-stats/team-radar-chart/team-radar-chart.component';
import { NgApexchartsModule } from 'ng-apexcharts';
import { TeamDialogComponent } from './components/team-stats/team-dialog/team-dialog.component';

@NgModule({
  declarations: [
    AlertComponent,
    HeaderComponent,
    ConfirmDialogComponent,
    RequestProblemDialogComponent,
    TeamStatsComponent,
    TeamChartComponent,
    TeamRadarChartComponent,
    TeamDialogComponent,
  ],
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatInputModule,
    MatOptionModule,
    MatSelectModule,
    ReactiveFormsModule,
    NgApexchartsModule,
  ],
  exports: [AlertComponent, HeaderComponent, TeamStatsComponent],
})
export class SharedModule {}
