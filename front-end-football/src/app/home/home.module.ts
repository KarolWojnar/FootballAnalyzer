import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    HomeRoutingModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatPaginatorModule,
    NgOptimizedImage,
    MatListModule,
    MatIconModule,
    ReactiveFormsModule,
  ],
  exports: [HomeComponent, MatTableModule, MatPaginatorModule],
})
export class HomeModule {}
