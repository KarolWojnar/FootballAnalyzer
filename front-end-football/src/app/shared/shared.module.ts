import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertComponent } from './components/alert/alert.component';
import { HeaderComponent } from './header/header.component';
import { RouterLink } from '@angular/router';

@NgModule({
  declarations: [AlertComponent, HeaderComponent],
  imports: [CommonModule, RouterLink],
  exports: [AlertComponent, HeaderComponent],
})
export class SharedModule {}
