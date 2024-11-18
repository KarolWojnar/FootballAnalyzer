import { Component, Input, OnChanges, ViewChild } from '@angular/core';

import {
  ApexAxisChartSeries,
  ApexChart,
  ApexFill,
  ApexMarkers,
  ApexStroke,
  ApexTheme,
  ApexTitleSubtitle,
  ApexXAxis,
  ChartComponent,
} from 'ng-apexcharts';
import { Stats } from '../../../../models/stats';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  title: ApexTitleSubtitle;
  stroke: ApexStroke;
  theme: ApexTheme;
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
  @Input() isDarkMode!: boolean;
  public chartOptions: ChartOptions = this.addExampleChart();

  ngOnChanges(): void {
    this.initializeRadarChart();
  }

  initializeRadarChart() {
    this.chartOptions = {
      series: [
        {
          name: this.teamStats.teamRating.team,
          data: [
            Math.round(this.teamStats.teamRating.aggression * 100000) / 1000,
            Math.round(this.teamStats.teamRating.attacking * 100000) / 1000,
            Math.round(this.teamStats.teamRating.creativity * 100000) / 1000,
            Math.round(this.teamStats.teamRating.defending * 100000) / 1000,
          ],
        },
        {
          name: this.teamStats.allTeamsRating.team
            ? this.teamStats.allTeamsRating.team
            : 'Średnia wszystkich drużyn',
          data: [
            Math.round(this.teamStats.allTeamsRating.aggression * 100000) /
              1000,
            Math.round(this.teamStats.allTeamsRating.attacking * 100000) / 1000,
            Math.round(this.teamStats.allTeamsRating.creativity * 100000) /
              1000,
            Math.round(this.teamStats.allTeamsRating.defending * 100000) / 1000,
          ],
        },
      ],
      chart: {
        height: 500,
        type: 'radar',
        foreColor: this.isDarkMode ? '#f1f1f1' : '#2a2f3b',
        background: this.isDarkMode ? '#1e1e1e' : '#fff',
      },
      theme: {
        mode: this.isDarkMode ? 'dark' : 'light',
        palette: 'palette5',
      },
      stroke: {
        width: 1,
      },
      fill: {
        opacity: 0.6,
      },
      markers: {
        size: 3,
      },
      xaxis: {
        categories: ['Agresja', 'Atak', 'Kreatywność', 'Obrona'],
      },
      title: {
        text: 'Radar statystyk',
        style: {
          color: this.isDarkMode ? '#f1f1f1' : '#2a2f3b',
          fontSize: '20px',
        },
      },
    };
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
      theme: {
        mode: 'light',
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
