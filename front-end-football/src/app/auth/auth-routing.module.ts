import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import {AccoutActivationComponent} from "./components/accout-activation/accout-activation.component";
import {ForgotPasswordComponent} from "./components/forgot-password/forgot-password.component";
import {PasswordRecoveryFormComponent} from "./components/password-recovery-form/password-recovery-form.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent, title: 'Login' },
  { path: 'register', component: RegisterComponent, title: 'Register' },
  { path: 'active/:uuid', component: AccoutActivationComponent, title: 'Activation' },
  { path: 'forgot-password', component: ForgotPasswordComponent, title: 'Forgot Password' },
  { path: 'forgot-password/:uuid', component: PasswordRecoveryFormComponent, title: 'Forgot Password' },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AuthRoutingModule {}
