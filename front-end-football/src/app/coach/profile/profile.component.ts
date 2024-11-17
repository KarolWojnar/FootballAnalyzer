import { Component } from '@angular/core';
import { ThemeService } from '../../services/theme.service';
import { map, Observable } from 'rxjs';
import { selectAuthUser } from '../../auth/store/auth.selectors';
import { Store } from '@ngrx/store';
import { AppState } from '../../store/app.reducer';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent {
  isDarkMode = false;
  userRoles$: Observable<string | null> = this.store
    .select(selectAuthUser)
    .pipe(map((user) => user?.roleName || null));

  constructor(
    private themeService: ThemeService,
    private store: Store<AppState>,
  ) {
    this.themeService.darkMode$.subscribe((isDarkMode) => {
      this.isDarkMode = isDarkMode;
    });
  }
}
