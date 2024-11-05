import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { ApiService } from '../../../services/api.service';

@Component({
  selector: 'app-accout-activation',
  templateUrl: './accout-activation.component.html',
  styleUrls: ['./accout-activation.component.scss'],
})
export class AccoutActivationComponent implements OnInit {
  error: string | null = null;
  success: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private apiService: ApiService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.route.paramMap
      .pipe(
        switchMap((params) =>
          this.apiService.activateAccount(params.get('uuid') as string),
        ),
      )
      .subscribe({
        next: (response) => {
          if (response.message) {
            this.success = response.message;
            setTimeout(() => {
              this.router.navigate(['/login']);
            }, 5000);
          }
        },
        error: (error) => {
          if (error.error.message) {
            this.error = error.error.message;
          }
        },
      });
  }
}
