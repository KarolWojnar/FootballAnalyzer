import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { PasswordRecoveryForm } from '../../../models/forms/forms.model';
import { FormService } from '../../../services/form/form.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-password-recovery-form',
  templateUrl: './password-recovery-form.component.html',
  styleUrls: ['./password-recovery-form.component.scss'],
})
export class PasswordRecoveryFormComponent implements OnInit {
  passwdRecoveryForm: FormGroup<PasswordRecoveryForm> =
    this.formService.initPwdRecoveryForm();
  hide = true;

  constructor(
    private formService: FormService,
    private route: ActivatedRoute,
  ) {}

  get controls(): PasswordRecoveryForm {
    return this.passwdRecoveryForm.controls;
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe({
      next: (params) => {
        const token = params.get('uuid');
        console.log(token);
      },
    });
  }

  getErrorMessage(control: FormControl<string>): string {
    return this.formService.getErrorMessage(control);
  }
}
