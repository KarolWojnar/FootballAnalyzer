import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { RequestsComponent } from './requests/requests.component';
import { TeamsComponent } from './teams/teams.component';
import { UsersComponent } from './users/users.component';
import { HomeModule } from '../home/home.module';
import { MatSelectModule } from '@angular/material/select';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { MatDialogModule} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";

@NgModule({
  declarations: [UsersComponent, RequestsComponent, TeamsComponent],
    imports: [
        CommonModule,
        AdminRoutingModule,
        HomeModule,
        MatSelectModule,
        MatExpansionModule,
        MatDialogModule,
        MatButtonModule,
        MatProgressSpinnerModule,
        ReactiveFormsModule,
        FormsModule,
        MatInputModule,
        MatIconModule,
    ],
})
export class AdminModule {}
