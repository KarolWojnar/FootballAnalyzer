import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { SharedModule } from '../shared/shared.module';
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import { AccoutActivationComponent } from './components/accout-activation/accout-activation.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { PasswordRecoveryFormComponent } from './components/password-recovery-form/password-recovery-form.component';

@NgModule({
  declarations: [LoginComponent, RegisterComponent, AccoutActivationComponent, ForgotPasswordComponent, PasswordRecoveryFormComponent],
    imports: [
        CommonModule,
        AuthRoutingModule,
        FormsModule,
        MatInputModule,
        MatButtonModule,
        MatIconModule,
        SharedModule,
        ReactiveFormsModule,
        MatProgressSpinnerModule,
    ],
  exports: [LoginComponent, RegisterComponent],
})
export class AuthModule {}
