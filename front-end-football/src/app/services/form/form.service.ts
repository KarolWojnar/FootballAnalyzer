import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  LoginForm,
  PasswordRecoveryForm,
  RecoveryPasswdForm,
  RegisterForm,
} from '../../models/forms/forms.model';
import { equivalentValidator } from '../../models/validatoros/equivalent.validator';

@Injectable({
  providedIn: 'root',
})
export class FormService {
  initLoginForm(): FormGroup<LoginForm> {
    return new FormGroup({
      login: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      password: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
    });
  }

  initRegisterForm(): FormGroup<RegisterForm> {
    return new FormGroup({
      firstName: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      lastName: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      login: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      email: new FormControl('', {
        validators: [Validators.required, Validators.email],
        nonNullable: true,
      }),
      password: new FormControl('', {
        validators: [Validators.required, Validators.minLength(8)],
        nonNullable: true,
      }),
      confirmPassword: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      roleId: new FormControl(0, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      teamId: new FormControl(0, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      checkBox: new FormControl(),
    });
  }

  initPasswdRecoveryForm(): FormGroup<RecoveryPasswdForm> {
    return new FormGroup({
      email: new FormControl('', {
        validators: [Validators.required, Validators.email],
        nonNullable: true,
      }),
    });
  }

  getErrorMessage(control: FormControl): string {
    if (control.hasError('required')) {
      return 'Pole nie może być puste.';
    } else if (control.hasError('email')) {
      return 'Nieprawidłowy format adresu e-mail.';
    } else if (control.hasError('minlength')) {
      return `Hasło musi zawierać co najmniej ${control.errors?.['minlength'].requiredLength} znaków.`;
    } else if (control.hasError('pattern')) {
      return 'Hasło musi zawierać co najmniej jedną dużą literę, jedną małą literę, jedną cyfrę i jeden znak specjalny.';
    } else if (control.hasError('passwordsNotEqual')) {
      return 'Hasła nie są zgodne.';
    }
    return '';
  }

  initPwdRecoveryForm(): FormGroup<PasswordRecoveryForm> {
    return new FormGroup(
      {
        password: new FormControl('', {
          validators: [Validators.required, Validators.minLength(8)],
          nonNullable: true,
        }),
        confirmPassword: new FormControl('', {
          validators: [Validators.required],
          nonNullable: true,
        }),
      },
      { validators: [equivalentValidator('password', 'confirmPassword')] },
    );
  }
}
