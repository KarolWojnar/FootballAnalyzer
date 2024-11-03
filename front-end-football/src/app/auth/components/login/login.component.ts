import { Component } from '@angular/core';
import {FormService} from "../../../services/form/form.service";
import {FormControl, FormGroup} from "@angular/forms";
import {LoginForm} from "../../../models/forms/forms.model";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  hide = true;
  loginForm: FormGroup<LoginForm> = this.formService.initLoginForm();

  constructor(private formService: FormService) {
  }

  get controls() {
    return this.loginForm.controls;
  }

  onLogin() {}

  getErrorMessage(control: FormControl): string {
    return this.formService.getErrorMessage(control);
  }
}
