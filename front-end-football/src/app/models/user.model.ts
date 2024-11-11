export interface IUser {
  email: string;
  login: string;
  firstname: string;
  lastname: string;
  teamId: number;
  roleName: string;
  teamLogo: string;
}

export interface UserLoginData {
  login: string;
  password: string;
}

export interface UserResponse {
  email: string;
  login: string;
  password: string;
  firstName: string;
  lastName: string;
  teamId: number;
  roleId: number;
}

export class User {
  constructor(
    public email: string,
    public login: string,
    public firstname: string,
    public lastname: string,
    public teamId: number,
    public roleName: string,
  ) {}
}

export interface AuthResponse {
  timestamp: string;
  message: string;
  code: string;
}

export interface ResetPassword {
  email: string;
}

export interface ChangePassword {
  password: string;
  uuid: string;
}

export interface LoggedIn extends Omit<AuthResponse, 'message'> {
  message: boolean;
}
