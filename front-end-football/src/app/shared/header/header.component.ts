import { Component } from '@angular/core';
import { AppState } from '../../store/app.reducer';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../auth/store/auth.actions';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  url: string = '';

  constructor(private store: Store<AppState>) {}

  onLogout() {
    this.store.dispatch(AuthActions.logout());
  }
}
