export interface UserLoginData {
  username: string;
  password: string;
}

export interface UserResponse {
  email: string;
  login: string;
  password: string;
  firstname: string;
  lastname: string;
  teamId: number;
  roleId: number;
}
