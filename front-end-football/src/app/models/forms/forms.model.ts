import { FormControl } from '@angular/forms';

export interface LoginForm {
  login: FormControl<string>;
  password: FormControl<string>;
}

export interface RecoveryPasswdForm {
  email: FormControl<string>;
}

export interface PasswordRecoveryForm {
  password: FormControl<string>;
  confirmPassword: FormControl<string>;
}

export interface RegisterForm {
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  login: FormControl<string>;
  email: FormControl<string>;
  password: FormControl<string>;
  confirmPassword: FormControl<string>;
  roleId: FormControl<number>;
  teamId: FormControl<number>;
  checkBox: FormControl<boolean>;
}

export interface PlayerStatsForm {
  startDate: FormControl<string>;
  endDate: FormControl<string>;
}

export interface TeamStatsForm {
  startDate: FormControl<string>;
  endDate: FormControl<string>;
  rounding: FormControl<string>;
}
