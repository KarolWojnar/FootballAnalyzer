import { User } from '../../models/user.model';
import { Action, createReducer, on } from '@ngrx/store';
import * as AuthActions from './auth.actions';

export interface AuthState {
  user: User | null;
  loading: boolean;
  error: string | null;
}

const initialState: AuthState = {
  user: null,
  loading: false,
  error: null,
};

const _authReducer = createReducer(
  initialState,
  on(AuthActions.login, (state) => ({
    ...state,
    loading: true,
  })),
  on(AuthActions.loginSuccess, (state, action) => ({
    ...state,
    user: new User(
      action.user.email,
      action.user.login,
      action.user.firstname,
      action.user.lastname,
      action.user.teamId,
      action.user.role,
    ),
    loading: false,
    error: null,
  })),
  on(AuthActions.loginFailure, (state, action) => ({
    ...state,
    error: action.error,
    loading: false,
  })),
  on(AuthActions.logout, (state) => ({
    ...state,
  })),
  on(AuthActions.logoutSuccess, (state) => ({
    ...state,
    user: null,
    error: null,
  })),
  on(AuthActions.logoutFailure, (state) => ({
    ...state,
  })),
  on(AuthActions.clearError, (state) => ({
    ...state,
    error: null,
  })),
);

export function authReducer(state: AuthState | undefined, action: Action) {
  return _authReducer(state, action);
}
