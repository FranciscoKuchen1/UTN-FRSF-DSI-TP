import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function greaterThanZeroValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (value <= 0) {
      return { greaterThanZero: true };
    }
    return null;
  };
}
