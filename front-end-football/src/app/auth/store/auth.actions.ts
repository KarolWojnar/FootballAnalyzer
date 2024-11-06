import { createAction, props } from '@ngrx/store';
import { IUser, UserLoginData } from '../../models/user.model';

const LOGIN_TYPE: string = '[Auth] Login';
const LOGOUT_TYPE: string = '[Auth] Logout';
const LOGIN_SUCCESS_TYPE: string = '[Auth] Login Success';
const LOGIN_FAILURE_TYPE: string = '[Auth] Login Failure';
const LOGOUT_SUCCESS_TYPE: string = '[Auth] Logout Success';
const LOGOUT_FAILURE_TYPE: string = '[Auth] Logout Failure';

const CLEAR_ERROR_TYPE: string = '[Auth] Register Failure';

export const login = createAction(
  LOGIN_TYPE,
  props<{ loginData: UserLoginData }>(),
);

export const loginSuccess = createAction(
  LOGIN_SUCCESS_TYPE,
  props<{ user: IUser }>(),
);

export const loginFailure = createAction(
  LOGIN_FAILURE_TYPE,
  props<{ error: string }>(),
);

export const logout = createAction(LOGOUT_TYPE);

export const logoutSuccess = createAction(LOGOUT_SUCCESS_TYPE);

export const logoutFailure = createAction(LOGOUT_FAILURE_TYPE);

export const clearError = createAction(CLEAR_ERROR_TYPE);
