import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const equivalentValidator = (
  passwdControlName: string,
  secondPasswordControlName: string,
): ValidatorFn => {
  return (group: AbstractControl): ValidationErrors | null => {
    const password = group.get(passwdControlName);
    const passwordConfirm = group.get(secondPasswordControlName);
    if (password?.value !== passwordConfirm?.value && passwordConfirm?.value) {
      passwordConfirm?.setErrors({ passwordsNotEqual: true });
    }
    return null;
  };
};
