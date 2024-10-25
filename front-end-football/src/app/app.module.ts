import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './shared/header/header.component';
import { AdminComponent } from './admin/admin.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClientModule } from '@angular/common/http';
import localePl from '@angular/common/locales/pl';
import { registerLocaleData } from '@angular/common';
import { MatSortModule } from '@angular/material/sort';
import { HomeModule } from './home/home.module';
import { AuthModule } from './auth/auth.module';
import { CoachModule } from './coach/coach.module';

registerLocaleData(localePl);

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    AdminComponent,
    PageNotFoundComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    MatSortModule,
    HomeModule,
    CoachModule,
    AuthModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
