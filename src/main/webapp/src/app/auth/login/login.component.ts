import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [
    FormsModule
  ],
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginData = { username: '', password: '' };

  constructor(private authService: AuthService) {}

  onSubmit() {
    this.authService.login(this.loginData).subscribe(
      response => {
        console.log('Logowanie zakończone sukcesem', response);
        localStorage.setItem('token', response.token); // Zapisz token JWT w lokalnym storage
      },
      error => {
        console.error('Logowanie nie powiodło się', error);
      }
    );
  }
}
