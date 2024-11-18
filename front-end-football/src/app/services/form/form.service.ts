import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  LoginForm,
  NewLeagueForm,
  PasswordRecoveryForm,
  PlayerStatsForm,
  RecoveryPasswdForm,
  RegisterForm,
  RequestProblemForm,
  TeamStatsForm,
  UserEditForm,
} from '../../models/forms/forms.model';
import { equivalentValidator } from '../../models/validatoros/equivalent.validator';
import { UserAdmin } from '../../models/user.model';

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
    return new FormGroup(
      {
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
        coachConfirmPdf: new FormControl(),
      },
      { validators: [equivalentValidator('password', 'confirmPassword')] },
    );
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

  startDatePlayer = new Date();
  endDatePlayer = new Date();

  startDateTeam = new Date();
  endDateTeam = new Date();

  startDateOpponent = new Date();
  endDateOpponent = new Date();

  setDates() {
    let startDatePlayersLocal = localStorage.getItem('startDatePlayer');
    let endDatePlayersLocal = localStorage.getItem('endDatePlayer');
    if (startDatePlayersLocal && endDatePlayersLocal) {
      this.startDatePlayer = new Date(startDatePlayersLocal);
      this.startDatePlayer.setMonth(this.startDatePlayer.getMonth() + 1);
      this.endDatePlayer = new Date(endDatePlayersLocal);
    }
    let startDateTeamLocal = localStorage.getItem('startDateTeam');
    let endDateTeamLocal = localStorage.getItem('endDateTeam');
    if (startDateTeamLocal && endDateTeamLocal) {
      this.startDateTeam = new Date(startDateTeamLocal);
      this.startDateTeam.setMonth(this.startDateTeam.getMonth() + 1);
      this.endDateTeam = new Date(endDateTeamLocal);
    }

    let startDateOpponentLocal = localStorage.getItem('startDateOpponent');
    let endDateOpponentLocal = localStorage.getItem('endDateOpponent');
    if (startDateOpponentLocal && endDateOpponentLocal) {
      this.startDateOpponent = new Date(startDateOpponentLocal);
      this.startDateOpponent.setMonth(this.startDateOpponent.getMonth() + 1);
      this.endDateOpponent = new Date(endDateOpponentLocal);
    }
  }

  initPlayerStatsForm(): FormGroup<PlayerStatsForm> {
    this.setDates();
    return new FormGroup({
      startDate: new FormControl(
        this.startDatePlayer.getFullYear() +
          (this.startDatePlayer.getMonth() > 9 ? '-' : '-0') +
          this.startDatePlayer.getMonth() +
          (this.startDatePlayer.getDate() > 9 ? '-' : '-0') +
          this.startDatePlayer.getDate(),
        {
          validators: [Validators.required],
          nonNullable: true,
        },
      ),
      endDate: new FormControl(
        this.endDatePlayer.getFullYear() +
          (this.endDatePlayer.getMonth() + 1 > 9 ? '-' : '-0') +
          (this.endDatePlayer.getMonth() + 1) +
          (this.endDatePlayer.getDate() > 9 ? '-' : '-0') +
          this.endDatePlayer.getDate(),
        {
          validators: [Validators.required],
          nonNullable: true,
        },
      ),
    });
  }

  initTeamStatsForm(): FormGroup<TeamStatsForm> {
    this.setDates();
    return new FormGroup({
      startDate: new FormControl(
        this.startDateTeam.getFullYear() +
          (this.startDateTeam.getMonth() > 9 ? '-' : '-0') +
          this.startDateTeam.getMonth() +
          (this.startDateTeam.getDate() > 9 ? '-' : '-0') +
          this.startDateTeam.getDate(),
        {
          validators: [Validators.required],
          nonNullable: true,
        },
      ),
      endDate: new FormControl(
        this.endDateTeam.getFullYear() +
          (this.endDateTeam.getMonth() + 1 > 9 ? '-' : '-0') +
          (this.endDateTeam.getMonth() + 1) +
          (this.endDateTeam.getDate() > 9 ? '-' : '-0') +
          this.endDateTeam.getDate(),
        {
          validators: [Validators.required],
          nonNullable: true,
        },
      ),
      rounding: new FormControl('week', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      compareToAll: new FormControl(true),
    });
  }

  initOpponentStatsForm(): FormGroup<TeamStatsForm> {
    this.setDates();
    return new FormGroup({
      startDate: new FormControl(
        this.startDateOpponent.getFullYear() +
          (this.startDateOpponent.getMonth() > 9 ? '-' : '-0') +
          this.startDateOpponent.getMonth() +
          (this.startDateOpponent.getDate() > 9 ? '-' : '-0') +
          this.startDateOpponent.getDate(),
        {
          validators: [Validators.required],
          nonNullable: true,
        },
      ),
      endDate: new FormControl(
        this.endDateOpponent.getFullYear() +
          (this.endDateOpponent.getMonth() + 1 > 9 ? '-' : '-0') +
          (this.endDateOpponent.getMonth() + 1) +
          (this.endDateOpponent.getDate() > 9 ? '-' : '-0') +
          this.endDateOpponent.getDate(),
        {
          validators: [Validators.required],
          nonNullable: true,
        },
      ),
      rounding: new FormControl('week', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      compareToAll: new FormControl(),
    });
  }

  initNewLeagueForm(): FormGroup<NewLeagueForm> {
    return new FormGroup({
      season: new FormControl(0, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      leagueId: new FormControl(0, {
        validators: [Validators.required],
        nonNullable: true,
      }),
    });
  }

  initNewUserDataForm(user: UserAdmin): FormGroup<UserEditForm> {
    return new FormGroup({
      id: new FormControl(user.id, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      firstName: new FormControl(user.firstName, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      lastName: new FormControl(user.lastName, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      email: new FormControl(user.email, {
        validators: [Validators.required, Validators.email],
        nonNullable: true,
      }),
      login: new FormControl(user.login, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      roleId: new FormControl(user.roleId ? user.roleId : 0, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      teamId: new FormControl(user.teamId ? user.teamId : -1, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      password: new FormControl('', {
        validators: [Validators.minLength(8)],
      }),
    });
  }

  initNewRequestForm(): FormGroup<RequestProblemForm> {
    return new FormGroup({
      id: new FormControl(0, {
        validators: [Validators.required],
        nonNullable: true,
      }),
      requestType: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      requestStatus: new FormControl('NOWE', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      requestData: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
    });
  }
}
