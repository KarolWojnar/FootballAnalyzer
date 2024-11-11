import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormService } from '../../../services/form/form.service';
import { FormControl, FormGroup } from '@angular/forms';
import { LoginForm } from '../../../models/forms/forms.model';
import { AppState } from '../../../store/app.reducer';
import { Store } from '@ngrx/store';
import * as AuthActions from '../../store/auth.actions';
import { clearError } from '../../store/auth.actions';
import { selectAuthError, selectAuthLoading } from '../../store/auth.selectors';
import { Observable } from 'rxjs';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnDestroy, OnInit {
  hide = true;
  loginForm: FormGroup<LoginForm> = this.formService.initLoginForm();
  errorMsg$: Observable<string | null> = this.store.select(selectAuthError);
  loading$: Observable<boolean> = this.store.select(selectAuthLoading);
  isDarkMode = true;

  constructor(
    private formService: FormService,
    private store: Store<AppState>,
    private themeService: ThemeService,
  ) {
    this.themeService.darkMode$.subscribe((mode) => {
      this.isDarkMode = mode;
    });
  }

  ngOnInit(): void {
    this.loading$ = this.store.select(selectAuthLoading);
  }

  get controls() {
    return this.loginForm.controls;
  }

  onLogin() {
    this.store.dispatch(
      AuthActions.login({ loginData: this.loginForm.getRawValue() }),
    );
  }

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }

  clearError() {
    this.store.dispatch(clearError());
  }

  ngOnDestroy(): void {
    this.store.dispatch(AuthActions.clearError());
  }
}
