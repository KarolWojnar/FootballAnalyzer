import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { AppState } from './store/app.reducer';
import * as AuthActions from './auth/store/auth.actions';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'front-end-football';
  isLogoNull = localStorage.getItem('logoUrl');

  constructor(private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(AuthActions.autoLogin());
  }
}
