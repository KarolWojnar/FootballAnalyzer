import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertComponent } from './components/alert/alert.component';
import { HeaderComponent } from './header/header.component';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [AlertComponent, HeaderComponent],
  imports: [CommonModule, RouterLink, RouterLinkActive, MatButtonModule],
  exports: [AlertComponent, HeaderComponent],
})
export class SharedModule {}
