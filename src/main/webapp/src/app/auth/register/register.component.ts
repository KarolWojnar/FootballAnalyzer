import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  standalone: true,
  imports: [
    FormsModule
  ],
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  registerData = { username: '', password: '' };

  constructor(private authService: AuthService) {}

  onSubmit() {
    this.authService.register(this.registerData).subscribe(
      response => {
        console.log('Rejestracja zakończona sukcesem', response);
      },
      error => {
        console.error('Rejestracja nie powiodła się', error);
      }
    );
  }
}
