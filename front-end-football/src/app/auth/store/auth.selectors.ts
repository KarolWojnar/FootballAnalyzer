import { AppState } from '../../store/app.reducer';
import { createSelector } from '@ngrx/store';

export const selectAuth = (state: AppState) => state.auth;

export const selectAuthUser = createSelector(selectAuth, (state) => state.user);

export const selectAuthError = createSelector(
  selectAuth,
  (state) => state.error,
);

export const selectAuthLoading = createSelector(
  selectAuth,
  (state) => state.loading,
);
