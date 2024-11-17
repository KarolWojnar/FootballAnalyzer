import { Component, OnInit } from '@angular/core';
import { ThemeService } from '../../../services/theme.service';
import { ApiService } from '../../../services/api.service';
import { MatDialog } from '@angular/material/dialog';
import { RequestProblem } from '../../../models/request/request';
import { RequestProblemDialogComponent } from '../../../shared/request-problem-dialog/request-problem-dialog.component';

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.scss'],
})
export class RequestsComponent implements OnInit {
  isDarkMode = false;
  alertMessage: string = '';
  filteredDataSource!: RequestProblem[];
  originalDataSource!: RequestProblem[];
  objectKeys = Object.keys;
  onlyNew = false;
  displayedColumns: string[] = [
    'id',
    'login',
    'requestType',
    'requestStatus',
    'requestData',
    'createdDate',
    'actions',
  ];
  isSubmitting = false;
  showAlert = false;
  dataSource!: RequestProblem[];
  buttonFilter = 'Nowe';

  constructor(
    private themeService: ThemeService,
    private apiService: ApiService,
    private dialog: MatDialog,
  ) {
    this.themeService.darkMode$.subscribe((isDarkMode) => {
      this.isDarkMode = isDarkMode;
    });
  }

  ngOnInit(): void {
    this.fetchRequests();
  }

  changeStatus(id: number, newStatus: string): void {
    const request = this.dataSource.find((r) => r.id === id);
    if (request) {
      request.requestStatus = newStatus;
    }
  }

  private fetchRequests() {
    this.apiService.getUserRequests().subscribe({
      next: (requests) => {
        requests.forEach((request) => {
          request.requestData = this.parseRequestData(
            request.requestData.toString(),
          );
        });
        this.dataSource = requests;
        this.originalDataSource = JSON.parse(JSON.stringify(requests));
        this.updateFilteredDataSource();
      },
      error: (error) => {
        this.showAlert = true;
        this.alertMessage = error.error.message;
      },
    });
  }

  updateFilteredDataSource() {
    if (this.onlyNew) {
      this.filteredDataSource = this.dataSource.filter(
        (request) => request.requestStatus === 'NOWE',
      );
      this.buttonFilter = 'Wszystkie';
    } else {
      this.filteredDataSource = this.dataSource;
      this.buttonFilter = 'Nowe';
    }
  }

  parseRequestData(requestData: string): { [key: string]: string } {
    const formattedString = requestData
      .replace(/[{}]/g, '"')
      .replace(/,/g, '","')
      .replace(/=/g, '":"');
    try {
      return JSON.parse(`{${formattedString}}`);
    } catch (e) {
      return {};
    }
  }

  onlyNewRequest() {
    this.onlyNew = !this.onlyNew;
    this.updateFilteredDataSource();
  }

  getChangedRequests(): RequestProblem[] {
    return this.dataSource.filter((request) => {
      const originalRequest = this.originalDataSource.find(
        (original) => original.id === request.id,
      );
      return (
        originalRequest &&
        request.requestStatus !== originalRequest.requestStatus
      );
    });
  }

  saveChanges() {
    this.isSubmitting = true;
    const changedRequests = this.getChangedRequests();

    if (changedRequests.length > 0) {
      changedRequests.forEach((request) => {
        this.apiService.updateRequest(request).subscribe({
          next: () => {
            this.isSubmitting = false;
            this.showAlert = true;
            this.alertMessage = 'Zmiany zostały zapisane pomyślnie!';
            this.originalDataSource = JSON.parse(
              JSON.stringify(this.dataSource),
            );
            this.fetchRequests();
            setTimeout(() => {
              this.showAlert = false;
            }, 5000);
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
      }, 5000);
    }
  }

  rollbackStatus(id: number) {
    const request = this.dataSource.find((r) => r.id === id);
    if (request) {
      request.requestStatus = this.originalDataSource.find(
        (original) => original.id === id,
      )!.requestStatus;
    }
  }

  sendRequest() {
    const dialogRef = this.dialog.open(RequestProblemDialogComponent, {
      width: '500px',
      data: { isDarkMode: this.isDarkMode },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.showAlert = true;
        this.alertMessage = result;
        setTimeout(() => {
          this.showAlert = false;
        }, 5000);
      }
    });
  }
}
