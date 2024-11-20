import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import { ApiService } from '../../services/api.service';
import { RequestProblem } from '../../models/request/request';
import { ConfirmDialogComponent } from '../../shared/components/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSort, Sort } from '@angular/material/sort';

@Component({
  selector: 'app-requests',
  templateUrl: './requests.component.html',
  styleUrls: ['./requests.component.scss'],
})
export class RequestsComponent implements OnInit, AfterViewInit {
  dataSource!: RequestProblem[];
  filteredDataSource!: RequestProblem[];
  originalDataSource!: RequestProblem[];
  objectKeys = Object.keys;
  isDarkMode = false;
  sortBy!: string;
  sortDirection!: string;

  onlyNew = false;
  @ViewChild(MatSort) sort!: MatSort;

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
  alertMessage = '';
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

  ngAfterViewInit(): void {
    if (this.sort) {
      this.sort.sortChange.subscribe((sort: Sort) => {
        this.getRequestes(sort.active, sort.direction);
      });
    }
  }

  getRequestes(sortBy?: string, sortDirection?: string): void {
    this.isSubmitting = true;
    this.apiService.getRequests(sortBy, sortDirection).subscribe({
      next: (dataSource) => {
        dataSource.forEach((request: RequestProblem) => {
          request.requestData = this.parseRequestData(
            request.requestData.toString(),
          );
        });
        this.dataSource = dataSource;
        this.originalDataSource = JSON.parse(JSON.stringify(dataSource));
        this.updateFilteredDataSource();
        this.isSubmitting = false;
      },
      error: (error) => {
        this.isSubmitting = false;
        this.showAlert = true;
        this.alertMessage = error.error.message;
      },
    });
  }

  ngOnInit(): void {
    this.getRequestes();
  }

  onSortChange(sort: Sort): void {
    if (this.sortBy == sort.active && this.sortDirection == sort.direction) {
      this.sortDirection = sort.direction == 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = sort.active;
      this.sortDirection = sort.direction;
      this.sort.direction = sort.direction == 'asc' ? 'desc' : 'asc';
    }
    this.getRequestes(this.sortBy, this.sortDirection);
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
      .replace(/,/g, '","')
      .replace(/=/g, '":"');
    try {
      return JSON.parse(`{${formattedString}}`);
    } catch (e) {
      return {};
    }
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
            this.getRequestes();
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

  onlyNewRequest() {
    this.onlyNew = !this.onlyNew;
    this.updateFilteredDataSource();
  }

  deleteRequest(id: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      data: {
        title: 'Potwierdzenie',
        button: 'Usuń',
        message: `Czy na pewno chcesz usunąć zgłoszenie?`,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.isSubmitting = true;
        this.apiService.deleteRequest(id).subscribe({
          next: () => {
            this.isSubmitting = false;
            this.showAlert = true;
            this.alertMessage = 'Zgłoszenie zostało usunięte pomyślnie!';
            this.getRequestes();
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
      }
    });
  }

  rollbackStatus(id: number) {
    const request = this.dataSource.find((r) => r.id === id);
    if (request) {
      request.requestStatus = this.originalDataSource.find(
        (original) => original.id === id,
      )!.requestStatus;
    }
  }
}
