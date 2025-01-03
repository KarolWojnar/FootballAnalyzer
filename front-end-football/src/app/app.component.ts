import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from './store/app.reducer';
import * as AuthActions from './auth/store/auth.actions';
import { ThemeService } from './services/theme.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'front-end-football';

  constructor(
    private store: Store<AppState>,
    private themeService: ThemeService,
  ) {
    this.themeService.darkMode$.subscribe((isDarkMode) => {});
  }

  ngOnInit(): void {
    this.store.dispatch(AuthActions.autoLogin());
  }
}
