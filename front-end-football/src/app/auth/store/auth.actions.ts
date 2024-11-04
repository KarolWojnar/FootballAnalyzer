import { createAction, props } from '@ngrx/store';
import { IUser, UserLoginData, UserResponse } from '../../models/user.model';
import { RequestProblem } from '../../models/request/request';

const LOGIN_TYPE: string = '[Auth] Login';
const LOGIN_SUCCESS_TYPE: string = '[Auth] Login Success';
const LOGIN_FAILURE_TYPE: string = '[Auth] Login Failure';

const REGISTER_TYPE: string = '[Auth] Register';
const REGISTER_SUCCESS_TYPE: string = '[Auth] Register Success';
const REGISTER_FAILURE_TYPE: string = '[Auth] Register Failure';
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

export const register = createAction(
  REGISTER_TYPE,
  props<{
    registerData: UserResponse;
    problem: RequestProblem;
    isSubmitting: boolean;
    alertMessage: string;
  }>(),
);

export const registerSuccess = createAction(
  REGISTER_SUCCESS_TYPE,
  props<{
    messageAlert: string;
    isSubmitting: boolean;
  }>(),
);

export const registerFailure = createAction(
  REGISTER_FAILURE_TYPE,
  props<{ error: string }>(),
);

export const clearError = createAction(CLEAR_ERROR_TYPE);
