import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  hide = true;

  regiseterForm = new FormGroup(
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
      role: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      country: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      league: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
      team: new FormControl('', {
        validators: [Validators.required],
        nonNullable: true,
      }),
    },
    { updateOn: 'submit' },
  );

  get controls() {
    return this.regiseterForm.controls;
  }

  ngOnInit(): void {
    this.controls;
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
    console.log(this.regiseterForm.getRawValue());
  }
}
