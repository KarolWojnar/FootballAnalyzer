import { Component, OnInit } from '@angular/core';
import { ThemeService } from '../../../services/theme.service';
import { UserAdmin } from '../../../models/user.model';
import { ApiService } from '../../../services/api.service';
import { FormControl, FormGroup } from '@angular/forms';
import { Team } from '../../../models/team/team';
import { Role } from '../../../auth/components/register/register.component';
import { FormService } from '../../../services/form/form.service';
import { UserEditForm } from '../../../models/forms/forms.model';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent implements OnInit {
  isDarkMode: boolean = false;
  logoUrl = localStorage.getItem('logoUrl');
  showAlert = false;
  alertMessage = '';
  user!: UserAdmin;
  fileControl = new FormControl(null);
  document!: File;
  isEditing = false;
  teams: Team[] = [];
  roles: Role[] = [];
  hide = true;
  userForm!: FormGroup<UserEditForm>;
  isSubmitting = false;

  constructor(
    private themeService: ThemeService,
    private apiService: ApiService,
    private formService: FormService,
  ) {
    this.themeService.darkMode$.subscribe((value) => {
      this.isDarkMode = value;
    });
  }

  ngOnInit(): void {
    this.fetchUser();
    this.fetchTeams();
    this.fetchRoles();
  }

  fetchTeams() {
    this.apiService.getTeams().subscribe({
      next: (teams: Team[]) => {
        this.teams = teams;
        this.teams.push({ id: -1, name: 'Brak' } as Team);
      },
      error: () => {},
    });
  }

  private fetchUser() {
    this.apiService.getUserData().subscribe({
      next: (response: UserAdmin) => {
        this.user = response;
      },
      error: () => {
        console.error('Błąd z pobieraniem danych użytkownika.');
      },
    });
  }

  saveUser() {
    this.showAlert = this.userForm.invalid;
    this.alertMessage = 'Formularz zawiera błędy';
    if (this.userForm.valid) {
      this.isSubmitting = true;
      this.userForm.value.id = this.user.id;
      this.apiService.updateUser(this.userForm.value).subscribe({
        next: () => {
          this.isSubmitting = false;
          this.isEditing = false;
          this.fetchUser();
        },
        error: (error) => {
          this.isSubmitting = false;
          this.showAlert = true;
          this.alertMessage = error.error.message;
        },
      });
    }
  }

  get controls() {
    return this.userForm.controls;
  }

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  onFileChange(event: any): void {
    const file = event.target.files[0];

    if (file) {
      if (file.type !== 'application/pdf') {
        this.alertMessage = 'Tylko pliki PDF są akceptowane.';
        this.showAlert = true;
        this.fileControl.setValue(null);
        return;
      }

      if (file.size > 5242880) {
        this.alertMessage = 'Plik jest za duży. Maksymalny rozmiar to 5MB.';
        this.showAlert = true;
        this.fileControl.setValue(null);
        return;
      }

      this.alertMessage = '';
      this.showAlert = false;
      this.document = file;
    }
  }

  getFileName(): string {
    const file = this.document;
    return file ? file.name : '';
  }

  sendDocument() {
    if (this.document) {
      const formData = new FormData();
      formData.append('file', this.document);
      if (this.document) {
        this.apiService.uploadFile(this.user.login, this.document).subscribe({
          next: () => {
            this.showAlert = true;
            this.alertMessage = 'Plik został wysłany';
            this.fileControl.setValue(null);
            this.document = null as unknown as File;
            setTimeout(() => {
              this.showAlert = false;
            }, 4000);
          },
          error: () => {
            this.showAlert = true;
            this.alertMessage = 'Wystąpił błąd podczas wysyłania pliku';
            setTimeout(() => {
              this.showAlert = false;
            }, 4000);
          },
        });
      }
    }
  }

  toggleEditMode() {
    this.isEditing = !this.isEditing;
    if (this.isEditing && this.user) {
      this.userForm = this.formService.initNewUserDataForm(this.user);
    }
  }

  private fetchRoles() {
    this.apiService.getRoles().subscribe({
      next: (roles: Role[]) => {
        this.roles = roles;
      },
      error: () => {},
    });
  }

  downloadFile() {
    this.apiService.downloadPdf(this.user.id).subscribe((response: Blob) => {
      const blob = new Blob([response], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = this.user.login + '_potwierdzenie.pdf';
      link.click();
    });
  }
}
