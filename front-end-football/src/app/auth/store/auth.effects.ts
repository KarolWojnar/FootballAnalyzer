import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { ApiService } from '../../services/api.service';
import * as AuthActions from './auth.actions';
import { catchError, map, of, switchMap } from 'rxjs';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class AuthEffects {
  constructor(
    private actions$: Actions,
    private apiService: ApiService,
    private router: Router,
  ) {}

  login$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.login),
      switchMap((action) => {
        return this.apiService.login(action.loginData).pipe(
          map((user) => {
            localStorage.setItem('logoUrl', user.teamLogo);
            this.router.navigate(['/home']);
            return AuthActions.loginSuccess({ user: { ...user } });
          }),
          catchError((error: HttpErrorResponse) => {
            console.log(error);
            let errorMessage;
            if (
              error.status >= 400 &&
              error.status < 500 &&
              error.error.message
            ) {
              errorMessage = error.error.message;
            } else {
              errorMessage = 'Wystąpił błąd. Spróbuj ponownie.';
            }
            return of(AuthActions.loginFailure({ error: errorMessage }));
          }),
        );
      }),
    );
  });

  autoLogin$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.autoLogin),
      switchMap(() => {
        return this.apiService.autoLogin().pipe(
          map((user) => {
            localStorage.setItem('logoUrl', user.teamLogo);
            return AuthActions.autoLoginSuccess({ user: { ...user } });
          }),
          catchError(() => {
            localStorage.clear();
            return of(AuthActions.autoLoginFailure());
          }),
        );
      }),
    ),
  );

  logout$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.logout),
      switchMap(() => {
        return this.apiService.logout().pipe(
          map(() => {
            this.router.navigate(['/login']);
            return AuthActions.logoutSuccess();
          }),
          catchError((error: unknown) => {
            if (
              error instanceof HttpErrorResponse &&
              error.status >= 400 &&
              error.status < 500 &&
              error.error.message
            ) {
            } else {
            }
            return of(AuthActions.logoutFailure());
          }),
        );
      }),
    );
  });
}
