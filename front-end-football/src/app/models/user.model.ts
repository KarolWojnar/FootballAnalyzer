export interface UserLoginData {
  username: string;
  password: string;
}

export interface UserResponse {
  id: number;
  email: string;
  username: string;
  password: string;
  name: {
    firstname: string;
    lastname: string;
  };
  token: string;
  role: string;
  team: {
    id: number;
    name: string;
    logo: string;
  };
}
