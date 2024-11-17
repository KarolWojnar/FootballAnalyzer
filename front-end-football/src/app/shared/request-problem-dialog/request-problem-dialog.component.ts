import { Component, Inject } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { FormService } from '../../services/form/form.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-request-problem-dialog',
  templateUrl: './request-problem-dialog.component.html',
  styleUrls: ['./request-problem-dialog.component.scss'],
})
export class RequestProblemDialogComponent {
  requestForm = this.formService.initNewRequestForm();
  isDarkMode = false;

  constructor(
    private formService: FormService,
    private apiService: ApiService,
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    public data: { isDarkMode: boolean },
  ) {
    this.isDarkMode = data.isDarkMode;
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onSubmit() {
    if (this.requestForm.valid) {
      this.requestForm.value.requestData =
        '{wiadomość=' + this.requestForm.value.requestData + '}';
      this.apiService.addRequest(this.requestForm.value).subscribe({
        next: () => {
          this.requestForm.reset();
          this.dialogRef.close('Dodano zgłoszenie');
        },
        error: () => {
          this.dialogRef.close(
            'Nie udało się dodać zgłoszenia. Spróbuj ponownie.',
          );
        },
      });
    }
  }
}
