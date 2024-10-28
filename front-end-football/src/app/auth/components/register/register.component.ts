import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../../../services/api.service';
import { Team } from '../../../models/team/team';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  hide = true;
  teams: Team[] = [];
  showAlert = false;
  alertMessage = '';

  constructor(
    private apiService: ApiService,
    private router: Router,
  ) {}

  registerForm = new FormGroup(
    {
      firstName: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      lastName: new FormControl('', {
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
      login: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      teamId: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
    },
    { updateOn: 'submit' },
  );

  get controls() {
    return this.registerForm.controls;
  }

  ngOnInit(): void {
    this.controls;
    this.getTeams();
  }

  getErrorMessage(control: FormControl) {
    if (control?.hasError('required')) {
      return 'Pole nie może byc puste';
    } else if (control?.hasError('minlength')) {
      return 'Hasło musi mieć co najmniej 8 znaków';
    } else if (control?.hasError('email')) {
      return 'Nieprawidłowy adres email';
    }
    return control?.hasError('') ? 'Not a valid email' : '';
  }

  onRegister() {
    this.showAlert = this.registerForm.invalid;
    this.alertMessage = 'Formularz zawiera błędy';
    console.log(this.registerForm.value);
    if (this.registerForm.valid) {
      this.apiService.register(this.registerForm.value).subscribe({
        next: (user) => {
          console.log('User registered successfully', user.login);
          this.router.navigate(['/login']).then();
        },
        error: ({ error }: { error: any }) => {
          this.alertMessage = error;
          this.showAlert = true;
        },
      });
    }
  }

  getTeams() {
    this.apiService.getTeams().subscribe((teams) => {
      this.teams = teams;
    });
  }
}
