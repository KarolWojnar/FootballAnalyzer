import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { RequestFormComponent } from './components/request-form/request-form.component';
import { UserFormComponent } from './components/user-form/user-form.component';
import { TeamFormComponent } from './components/team-form/team-form.component';

@NgModule({
  declarations: [RequestFormComponent, UserFormComponent, TeamFormComponent],
  imports: [CommonModule, AdminRoutingModule],
})
export class AdminModule {}
