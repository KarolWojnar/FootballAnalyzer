import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ChartOptions } from '../../../shared/components/team-stats/team-radar-chart/team-radar-chart.component';

@Component({
  selector: 'app-player-radar-modal',
  templateUrl: './player-radar-modal.component.html',
  styleUrls: ['./player-radar-modal.component.scss'],
})
export class PlayerRadarModalComponent implements OnInit {
  chartOptions!: ChartOptions;
  isDarkMode: boolean;

  constructor(@Inject(MAT_DIALOG_DATA) data: any) {
    this.chartOptions = data.chartOptions;
    this.isDarkMode = data.isDarkMode;
  }

  ngOnInit(): void {
    console.log(this.chartOptions);
  }
}
