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

@NgModule({
  declarations: [LoginComponent, RegisterComponent],
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
