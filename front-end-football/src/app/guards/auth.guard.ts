import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { catchError, map, Observable, of, take } from 'rxjs';
import { ApiService } from '../services/api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private apiService: ApiService,
    private router: Router,
  ) {}

  canActivate():
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return this.apiService.isLoggedIn().pipe(
      take(1),
      map((res) => {
        const isLoggedIn = res.message;
        if (!isLoggedIn) {
          this.router.navigate(['/home']);
          return false;
        }
        return true;
      }),
      catchError(() => {
        return of(true);
      }),
    );
  }
}
