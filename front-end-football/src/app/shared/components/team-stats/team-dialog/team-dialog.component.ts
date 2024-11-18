import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ThemeService } from '../../../../services/theme.service';

@Component({
  selector: 'app-team-dialog',
  templateUrl: './team-dialog.component.html',
  styleUrls: ['./team-dialog.component.scss'],
})
export class TeamDialogComponent {
  teamForm;
  isDarkMode = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<TeamDialogComponent>,
    private themeService: ThemeService,
  ) {
    this.teamForm = this.fb.group({
      kraj: ['', Validators.required],
      liga: ['', Validators.required],
      drużyna: ['', Validators.required],
    });
    this.themeService.darkMode$.subscribe((isDarkMode) => {
      this.isDarkMode = isDarkMode;
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
