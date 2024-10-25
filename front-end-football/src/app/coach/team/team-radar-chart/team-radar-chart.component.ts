import { Component, Input, OnChanges, ViewChild } from '@angular/core';

import {
  ApexAxisChartSeries,
  ApexChart,
  ApexFill,
  ApexMarkers,
  ApexStroke,
  ApexTitleSubtitle,
  ApexXAxis,
  ChartComponent,
} from 'ng-apexcharts';
import { Stats } from '../../../models/stats';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  title: ApexTitleSubtitle;
  stroke: ApexStroke;
  fill: ApexFill;
  markers: ApexMarkers;
  xaxis: ApexXAxis;
};

@Component({
  selector: 'app-team-radar-chart',
  templateUrl: './team-radar-chart.component.html',
  styleUrls: ['./team-radar-chart.component.scss'],
})
export class TeamRadarChartComponent implements OnChanges {
  @ViewChild('chart') chart!: ChartComponent;
  @Input() teamStats!: Stats;
  public chartOptions: ChartOptions = this.addExampleChart();

  ngOnChanges(): void {
    this.initializeRadarChart();
  }

  initializeRadarChart() {
    this.cutNumbers();
    this.chartOptions = {
      series: [
        {
          name: this.teamStats.teamRating.team,
          data: [
            this.teamStats.teamRating.aggression,
            this.teamStats.teamRating.attacking,
            this.teamStats.teamRating.creativity,
            this.teamStats.teamRating.defending,
          ],
        },
        {
          name: 'Średnia wszystkich drużyn',
          data: [
            this.teamStats.allTeamsRating.aggression,
            this.teamStats.allTeamsRating.attacking,
            this.teamStats.allTeamsRating.creativity,
            this.teamStats.allTeamsRating.defending,
          ],
        },
      ],
      chart: {
        foreColor: 'white',
        height: 600,
        type: 'radar',
        dropShadow: {
          opacity: 0.5,
          enabled: true,
          blur: 1,
          left: 1,
          top: 1,
        },
      },
      title: {
        text: 'Radar statystyk',
        style: {
          color: 'white',
        },
      },
      stroke: {
        width: 0,
      },
      fill: {
        opacity: 0.4,
      },
      markers: {
        size: 2,
      },
      xaxis: {
        categories: ['Agresja', 'Atak', 'Kreatywność', 'Obrona'],
      },
    };
  }

  private cutNumbers() {
    this.teamStats.teamRating.defending =
      Math.floor(this.teamStats.teamRating.defending * 10000) / 100;
    this.teamStats.allTeamsRating.defending =
      Math.floor(this.teamStats.allTeamsRating.defending * 10000) / 100;
    this.teamStats.teamRating.creativity =
      Math.floor(this.teamStats.teamRating.creativity * 10000) / 100;
    this.teamStats.allTeamsRating.creativity =
      Math.floor(this.teamStats.allTeamsRating.creativity * 10000) / 100;
    this.teamStats.teamRating.attacking =
      Math.floor(this.teamStats.teamRating.attacking * 10000) / 100;
    this.teamStats.allTeamsRating.attacking =
      Math.floor(this.teamStats.allTeamsRating.attacking * 10000) / 100;
    this.teamStats.teamRating.aggression =
      Math.floor(this.teamStats.teamRating.aggression * 10000) / 100;
    this.teamStats.allTeamsRating.aggression =
      Math.floor(this.teamStats.allTeamsRating.aggression * 10000) / 100;
  }

  private addExampleChart(): ChartOptions {
    return {
      series: [
        {
          name: 'Series Blue',
          data: [80, 50, 30, 40, 100, 20],
        },
        {
          name: 'Series Green',
          data: [20, 30, 40, 80, 20, 80],
        },
        {
          name: 'Series Orange',
          data: [44, 76, 78, 13, 43, 10],
        },
      ],
      chart: {
        height: 350,
        type: 'radar',
        dropShadow: {
          enabled: true,
          blur: 1,
          left: 1,
          top: 1,
        },
      },
      title: {
        text: 'Radar Chart - Multi Series',
      },
      stroke: {
        width: 0,
      },
      fill: {
        opacity: 0.4,
      },
      markers: {
        size: 0,
      },
      xaxis: {
        categories: ['2011', '2012', '2013', '2014', '2015', '2016'],
      },
    };
  }
}
