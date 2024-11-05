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
            this.router.navigate(['/home']);
            return AuthActions.loginSuccess({ user: { ...user } });
          }),
          catchError((error: HttpErrorResponse) => {
            console.log(error);
            let errorMessage = '';
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

  logout$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.logout),
      switchMap(() => {
        return this.apiService.logout().pipe(
          map(() => {
            this.router.navigate(['/login']);
            return AuthActions.logoutSuccess();
          }),
          catchError((error) => {
            let errorMessage = '';
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
}
