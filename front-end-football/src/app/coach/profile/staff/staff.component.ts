import { Component, OnInit } from '@angular/core';
import { UserStaff } from '../../../models/user.model';
import { ThemeService } from '../../../services/theme.service';
import { ApiService } from '../../../services/api.service';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import * as AuthActions from '../../../auth/store/auth.actions';
import { Store } from '@ngrx/store';
import { AppState } from '../../../store/app.reducer';

@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrls: ['./staff.component.scss'],
})
export class StaffComponent implements OnInit {
  users: UserStaff[] = [];
  isDarkMode = false;
  message: string = '';
  showMessage: boolean = false;

  constructor(
    private themeService: ThemeService,
    private apiService: ApiService,
    private dialog: MatDialog,
    private store: Store<AppState>,
  ) {
    this.themeService.darkMode$.subscribe((isDarkMode) => {
      this.isDarkMode = isDarkMode;
    });
  }

  ngOnInit(): void {
    this.fetchStaff();
  }

  removeFromTeam(user: UserStaff) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      data: {
        title: 'Potwierdzenie',
        button: 'Usuń',
        message: `Czy na pewno chcesz usunąć użytkownika ${user.login}?`,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.apiService.removeFromTeam(user.id).subscribe({
          next: () => {
            this.fetchStaff();
          },
          error: (error) => {
            this.showMessage = true;
            this.message = error.error.message;
          },
        });
      }
    });
  }

  setAsCoach(user: UserStaff) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '300px',
      data: {
        title: 'Potwierdzenie',
        button: 'Potwierdź',
        message: `Czy na pewno chcesz mianować ${user.login} trenerem głównym? Jeśli to zrobisz zostaniesz wylogowany i stracisz prawa trenera.`,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.apiService.setAsCoach(user.id).subscribe({
          next: () => {
            localStorage.clear();
            this.store.dispatch(AuthActions.logout());
          },
          error: (error) => {
            this.showMessage = true;
            this.message = error.error.message;
          },
        });
      }
    });
  }

  private fetchStaff() {
    this.apiService.getStaff().subscribe({
      next: (users: UserStaff[]) => {
        this.users = users;
        if (this.users == null) {
          this.showMessage = true;
          this.message = 'Brak sztabu szkoleniowego.';
        }
      },
      error: (error) => {
        if (error.status === 404) {
          this.users = [];
          this.showMessage = true;
          this.message = 'Brak sztabu szkoleniowego.';
          return;
        } else {
          this.showMessage = true;
          this.message = error.message;
        }
      },
    });
  }
}
