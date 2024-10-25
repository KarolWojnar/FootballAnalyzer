import { Component } from '@angular/core';
import { UserLoginData } from '../../../models/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  hide = true;
  userData: UserLoginData = {
    username: '',
    password: '',
  };

  onLogin() {}
}
