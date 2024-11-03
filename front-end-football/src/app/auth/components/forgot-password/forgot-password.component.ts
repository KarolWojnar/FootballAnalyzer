import { Component } from '@angular/core';
import {FormControl, FormGroup } from "@angular/forms";
import {FormService} from "../../../services/form/form.service";
import {RecoveryPasswdForm} from "../../../models/forms/forms.model";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
  passwdRecoveryForm: FormGroup<RecoveryPasswdForm> = this.formService.initPasswdRecoveryForm();

    constructor(private formService: FormService) { }

  get controls() {
    return this.passwdRecoveryForm.controls;
  }

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }
}
