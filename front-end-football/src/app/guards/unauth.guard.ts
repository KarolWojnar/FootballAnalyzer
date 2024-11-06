import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { catchError, map, Observable, of, take } from 'rxjs';
import { ApiService } from '../services/api.service';

@Injectable({
  providedIn: 'root',
})
export class UnauthGuard implements CanActivate {
  constructor(
    private apiService: ApiService,
    private router: Router,
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return this.apiService.isLoggedIn().pipe(
      take(1),
      map((res) => {
        const isLoggedIn = res.message;
        if (isLoggedIn) {
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
