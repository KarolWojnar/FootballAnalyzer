import { Component, Input, OnChanges, OnInit, ViewChild } from '@angular/core';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexTooltip,
  ApexStroke,
} from 'ng-apexcharts';
import { Stats } from '../../models/stats';

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  xaxis: ApexXAxis;
  stroke: ApexStroke;
  tooltip: ApexTooltip;
  dataLabels: ApexDataLabels;
};

@Component({
  selector: 'app-team-chart',
  templateUrl: './team-chart.component.html',
  styleUrls: ['./team-chart.component.scss'],
})
export class TeamChartComponent implements OnChanges {
  @ViewChild('chart') chart!: ChartComponent;
  @Input() teamStats!: Stats;
  public chartOptions: ChartOptions = this.addExampleChart();
  sortedDates: string[] = [];
  myTeamForm: number[] = [];
  allTeamForm: number[] = [];

  ngOnChanges(): void {
    const sortedDates = Object.keys(this.teamStats.teamForm.rating).sort(
      (a, b) => new Date(a).getTime() - new Date(b).getTime(),
    );
    const teamData = sortedDates.map(
      (date) => this.teamStats.teamForm.rating[date],
    );
    const allTeamsData = sortedDates.map(
      (date) => this.teamStats.allTeamsForm.rating[date] ?? 0,
    );
    this.sortedDates = sortedDates;
    this.myTeamForm = teamData;
    this.allTeamForm = allTeamsData;
    this.initializeDataChart();
  }

  initializeDataChart() {
    this.chartOptions = {
      series: [
        {
          name: this.teamStats.teamForm.team
            ? this.teamStats.teamForm.team
            : 'NaN',
          data: this.myTeamForm.length > 0 ? this.myTeamForm : [],
        },
        {
          name: 'Średnia wszystkich drużyn',
          data: this.allTeamForm.length > 0 ? this.allTeamForm : [],
        },
      ],
      chart: {
        height: 500,
        type: 'area',
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'smooth',
      },
      xaxis: {
        type: 'category',
        categories: this.sortedDates.length > 0 ? this.sortedDates : [],
      },
      tooltip: {
        x: {
          format: 'dd/MM/yy',
        },
      },
    };
  }

  private addExampleChart(): ChartOptions {
    return {
      series: [
        {
          name: 'MyTeam',
          data: [1.0, 2.0, 1.0, 3.0],
        },
        {
          name: 'All Teams',
          data: [1.0, 2.0, 1.0, 3.0],
        },
      ],
      chart: {
        height: 100,
        type: 'area',
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'smooth',
      },
      xaxis: {
        type: 'datetime',
        categories: ['2022-09-25', '2022-09-25', '2022-09-25', '2022-09-25'],
      },
      tooltip: {
        x: {
          format: 'dd/MM/yy',
        },
      },
    };
  }
}