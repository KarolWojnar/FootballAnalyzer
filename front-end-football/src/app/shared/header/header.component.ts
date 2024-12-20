import { Component } from '@angular/core';
import { AppState } from '../../store/app.reducer';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../auth/store/auth.actions';
import { map, Observable } from 'rxjs';
import { User } from '../../models/user.model';
import { selectAuthUser } from '../../auth/store/auth.selectors';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  isDarkMode = true;
  url: string = '';
  user$: Observable<User | null> = this.store.select(selectAuthUser);
  userRoles$: Observable<string | null> = this.store
    .select(selectAuthUser)
    .pipe(map((user) => user?.roleName || null));

  constructor(
    private store: Store<AppState>,
    private themeService: ThemeService,
  ) {
    this.themeService.darkMode$.subscribe((isDarkMode) => {
      this.isDarkMode = isDarkMode;
    });
  }

  onLogout() {
    localStorage.clear();
    this.store.dispatch(AuthActions.logout());
  }

  toggleDarkMode() {
    this.themeService.toggleDarkMode();
  }
}
