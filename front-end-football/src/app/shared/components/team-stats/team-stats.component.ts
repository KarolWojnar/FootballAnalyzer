import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ThemeService } from '../../../services/theme.service';
import { FormGroup } from '@angular/forms';
import { TeamStatsForm } from '../../../models/forms/forms.model';

@Component({
  selector: 'app-team-stats',
  templateUrl: './team-stats.component.html',
  styleUrls: ['./team-stats.component.scss'],
})
export class TeamStatsComponent {
  isDarkMode: boolean = false;
  formVisible: boolean = true;
  @Input() teamStats: any;
  @Input() form!: FormGroup;
  @Output() formSubmit = new EventEmitter<TeamStatsForm>();
  alertMessage = '';
  selectedChart = 'line';

  constructor(private themeService: ThemeService) {
    this.themeService.darkMode$.subscribe((isDark) => {
      this.isDarkMode = isDark;
    });
  }

  logoUrl = localStorage.getItem('logoUrl');
  @Input() compareToAll!: boolean;

  toggleForm(): void {
    this.formVisible = !this.formVisible;
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.formSubmit.emit(this.form.value);
    }
  }

  selectChart(chartType: string): void {
    this.selectedChart = chartType;
  }
}
