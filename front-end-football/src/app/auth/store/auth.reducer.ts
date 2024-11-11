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
      action.user.roleName,
    ),
    loading: false,
    error: null,
  })),
  on(AuthActions.loginFailure, (state, action) => ({
    ...state,
    error: action.error,
    loading: false,
  })),
  on(
    AuthActions.logout,
    AuthActions.logoutFailure,
    AuthActions.autoLoginFailure,
    AuthActions.autoLogin,
    (state) => ({
      ...state,
    }),
  ),
  on(AuthActions.logoutSuccess, (state) => ({
    ...state,
    user: null,
    error: null,
  })),
  on(AuthActions.clearError, (state) => ({
    ...state,
    error: null,
  })),
  on(AuthActions.autoLoginSuccess, (state, action) => ({
    ...state,
    user: new User(
      action.user.email,
      action.user.login,
      action.user.firstname,
      action.user.lastname,
      action.user.teamId,
      action.user.roleName,
    ),
  })),
);

export function authReducer(state: AuthState | undefined, action: Action) {
  return _authReducer(state, action);
}
