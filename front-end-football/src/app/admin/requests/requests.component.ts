import { Component, OnInit } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import { ApiService } from '../../services/api.service';
import { RequestProblem } from '../../models/request/request';

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.scss'],
})
export class RequestsComponent implements OnInit {
  dataSource!: RequestProblem[];
  objectKeys = Object.keys;
  isDarkMode = false;
  displayedColumns: string[] = [
    'id',
    'login',
    'requestType',
    'requestStatus',
    'requestData',
    'actions',
  ];
  isSubmitting = false;
  showAlert = false;
  alertMessage = '';

  constructor(
    private themeService: ThemeService,
    private apiService: ApiService,
  ) {
    this.themeService.darkMode$.subscribe((isDarkMode) => {
      this.isDarkMode = isDarkMode;
    });
  }

  ngOnInit(): void {
    this.isSubmitting = true;
    this.apiService.getRequests().subscribe({
      next: (dataSource) => {
        dataSource.forEach((request: RequestProblem) => {
          request.requestData = this.parseRequestData(
            request.requestData.toString(),
          );
        });
        this.dataSource = dataSource;
        this.isSubmitting = false;
      },
      error: (error) => {
        this.isSubmitting = false;
        this.showAlert = true;
        this.alertMessage = error.error.message;
      },
    });
  }

  changeStatus(id: number, newStatus: string): void {
    const request = this.dataSource.find((r) => r.id === id);
    if (request) {
      request.requestStatus = newStatus;
    }
  }

  parseRequestData(requestData: string): { [key: string]: string } {
    const formattedString = requestData
      .replace(/[{}]/g, '"')
      .replace(/\s+/g, '')
      .replace(/,/g, '","')
      .replace(/=/g, '":"');
    try {
      return JSON.parse(`{${formattedString}}`);
    } catch (e) {
      return {};
    }
  }

  saveChanges() {}
}
