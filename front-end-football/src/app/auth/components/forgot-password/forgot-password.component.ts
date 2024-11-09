import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { FormService } from '../../../services/form/form.service';
import { RecoveryPasswdForm } from '../../../models/forms/forms.model';
import { ApiService } from '../../../services/api.service';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss'],
})
export class ForgotPasswordComponent {
  passwdRecoveryForm: FormGroup<RecoveryPasswdForm> =
    this.formService.initPasswdRecoveryForm();
  errorMessage: null | string = null;
  successMessage: null | string = null;
  loading = false;
  isDarkMode = true;

  constructor(
    private formService: FormService,
    private apiService: ApiService,
    private themeService: ThemeService,
  ) {
    this.themeService.darkMode$.subscribe((mode) => {
      this.isDarkMode = mode;
    });
  }

  get controls() {
    return this.passwdRecoveryForm.controls;
  }

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }

  onPasswordRecover() {
    this.loading = true;
    this.apiService
      .resetPassword(this.passwdRecoveryForm.getRawValue())
      .subscribe({
        next: (param) => {
          console.log(param);
          this.loading = false;
          this.successMessage =
            'Na podany email został wysłany link do zmiany hasła.';
          this.errorMessage = null;
        },
        error: (error) => {
          console.log(error);
          this.loading = false;
          this.errorMessage = error.error.message;
          this.successMessage = null;
        },
      });
  }
}
