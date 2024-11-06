import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { PasswordRecoveryForm } from '../../../models/forms/forms.model';
import { FormService } from '../../../services/form/form.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../../services/api.service';
import { BehaviorSubject, interval, takeWhile } from 'rxjs';

@Component({
  selector: 'app-password-recovery-form',
  templateUrl: './password-recovery-form.component.html',
  styleUrls: ['./password-recovery-form.component.scss'],
})
export class PasswordRecoveryFormComponent implements OnInit {
  passwdRecoveryForm: FormGroup<PasswordRecoveryForm> =
    this.formService.initPwdRecoveryForm();
  hide = true;
  uuid = '';
  loading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;
  progress$ = new BehaviorSubject<number>(100);
  private progress = 100;

  constructor(
    private formService: FormService,
    private route: ActivatedRoute,
    private apiService: ApiService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.updateProgressBar();
    this.route.paramMap.subscribe({
      next: (params) => {
        this.uuid = params.get('uuid') as string;
      },
    });
  }

  get controls(): PasswordRecoveryForm {
    return this.passwdRecoveryForm.controls;
  }

  getErrorMessage(control: FormControl<string>): string {
    return this.formService.getErrorMessage(control);
  }

  onNewPassword() {
    this.loading = true;
    const { password } = this.passwdRecoveryForm.getRawValue();
    this.apiService
      .changePassword({
        uuid: this.uuid,
        password,
      })
      .subscribe({
        next: (response) => {
          this.loading = false;
          this.successMessage = response.message;
          this.progress$?.complete();
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = error.error.message;
          this.progress$?.complete();
        },
      });
  }

  private updateProgressBar() {
    const totalDuration = 15 * 60;
    const decrementInterval = 1000;
    const decrementValue = 100 / totalDuration;

    interval(decrementInterval)
      .pipe(takeWhile(() => this.progress > 0))
      .subscribe(() => {
        this.progress -= decrementValue;
        this.progress$.next(this.progress);
      });
  }
}
