import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { PasswordRecoveryForm } from '../../../models/forms/forms.model';
import { FormService } from '../../../services/form/form.service';

@Component({
  selector: 'app-password-recovery-form',
  templateUrl: './password-recovery-form.component.html',
  styleUrls: ['./password-recovery-form.component.scss'],
})
export class PasswordRecoveryFormComponent {
  passwdRecoveryForm: FormGroup<PasswordRecoveryForm> =
    this.formService.initPwdRecoveryForm();
  hide = true;

  constructor(private formService: FormService) {}

  get controls(): PasswordRecoveryForm {
    return this.passwdRecoveryForm.controls;
  }

  getErrorMessage(control: FormControl<string>): string {
    return this.formService.getErrorMessage(control);
  }
}
