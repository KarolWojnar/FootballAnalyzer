import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-team-dialog',
  templateUrl: './team-dialog.component.html',
  styleUrls: ['./team-dialog.component.scss'],
})
export class TeamDialogComponent {
  teamForm;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TeamDialogComponent>,
  ) {
    this.teamForm = this.fb.group({
      country: ['', Validators.required],
      leagueName: ['', Validators.required],
      teamName: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.teamForm.valid) {
      this.dialogRef.close(this.teamForm.value);
    }
  }

  onCancel() {
    this.teamForm.reset();
    this.dialogRef.close();
  }
}
