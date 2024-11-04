import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { ApiService } from '../../services/api.service';
import * as AuthActions from './auth.actions';
import { catchError, map, of, switchMap } from 'rxjs';
import { Router } from '@angular/router';
import { RequestProblem } from '../../models/request/request';

@Injectable()
export class AuthEffects {
  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.login),
      switchMap((action) => {
        return this.apiService.login(action.loginData).pipe(
          map((user) => AuthActions.loginSuccess({ user: { ...user } })),
          catchError(() =>
            of(AuthActions.loginFailure({ error: 'Wystąpił błąd' })),
          ),
        );
      }),
    ),
  );

  register$ = createEffect(() =>
    this.actions$.pipe(
      ofType(AuthActions.register),
      switchMap((action) => {
        return this.apiService.register(action.registerData).pipe(
          map(() => {
            action.isSubmitting = false;
            action.alertMessage = 'Konto zostało pomyślnie założone!';
            if (action.problem.requestData) {
              this.handleNewRequest(action.problem);
            }
            this.router.navigate(['/login']);
            return AuthActions.registerSuccess({
              messageAlert: action.alertMessage,
              isSubmitting: action.isSubmitting,
            });
          }),
          catchError((err) => {
            return of(AuthActions.registerFailure({ error: err }));
          }),
        );
      }),
    ),
  );

  constructor(
    private actions$: Actions,
    private apiService: ApiService,
    private router: Router,
  ) {}

  handleNewRequest(request: RequestProblem) {
    this.apiService.addRequest(request).subscribe({
      next: () => console.log('Request o nową drużynę został wysłany.'),
      error: (err) =>
        console.error('Błąd przy wysyłaniu requesta o nową drużynę:', err),
    });
  }
}
