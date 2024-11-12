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
  originalDataSource!: RequestProblem[];
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
        this.originalDataSource = JSON.parse(JSON.stringify(dataSource));
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

  getChangedRequests(): RequestProblem[] {
    return this.dataSource.filter((request, index) => {
      return (
        request.requestStatus !== this.originalDataSource[index].requestStatus
      );
    });
  }

  saveChanges() {
    this.isSubmitting = true;
    console.log(this.dataSource);
    console.log(this.originalDataSource);
    const changedRequests = this.getChangedRequests();
    console.log(changedRequests.length);
    if (changedRequests.length > 0) {
      changedRequests.forEach((request) => {
        this.apiService.updateRequest(request).subscribe({
          next: () => {
            this.isSubmitting = false;
            this.showAlert = true;
            this.alertMessage = 'Zmiany zostały zapisane pomyślnie!';
          },
          error: (error) => {
            this.isSubmitting = false;
            this.showAlert = true;
            this.alertMessage = error.error.message;
          },
        });
      });
    } else {
      this.isSubmitting = false;
      this.showAlert = true;
      this.alertMessage = 'Brak zmienionych zgłoszeń do zapisania.';
      setTimeout(() => {
        this.showAlert = false;
      }, 3000);
    }
  }
}
