import {Component} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  UntypedFormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {Router} from "@angular/router";
import {Turnos} from "../../../interfaces/turnos";
import {AlertService} from "../../../services/alert/alert.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-registrar-bedel',
  templateUrl: './registrar-bedel.component.html',
  styleUrls: ['./registrar-bedel.component.scss']
})
export class RegistrarBedelComponent {

  bedelForm: UntypedFormGroup;
  turnos: Turnos[] = [{id: 0, name: 'Mañana'}, {id: 1, name: 'Tarde'}, {id: 2, name: 'Noche'}];

  isLengthValid = false;
  isSamePass = false;
  hasUppercase = false;
  hasNumber = false;
  hasSpecialCharacter = false;

  constructor(
    private formbuilder: FormBuilder,
    private router: Router,
    private alertService: AlertService,
    private http: HttpClient,
  ) {
    this.bedelForm = this.formbuilder.group({
      idRegistro: [null, Validators.required],
      nombre: [null, Validators.required],
      apellido: [null, Validators.required],
      tipoTurno: [null, Validators.required],
      contrasena: [null, this.passValidator()],
      confirmacionContrasena: [null, this.passValidator()]
    }, {validators: this.passMatchValidator});
  }

  redirect(url: string) {
    this.router.navigate([url]);
  }

  close(): void {
    this.alertService.confirm('Cancelar', 'Desea cancelar el registro de bedel?').subscribe(() => {
      this.redirect('');
    });
  }

  checkPasswordCriteria(): void {
    const value1 = this.bedelForm.get('contrasena')?.value;
    const value2 = this.bedelForm.get('confirmacionContrasena')?.value;

    this.isLengthValid = (value1.length >= 6 && value1.length <= 20);
    this.hasUppercase = /[A-Z]/.test(value1);
    this.hasNumber = /[0-9]/.test(value1);
    this.hasSpecialCharacter = /[@#$%&*]/.test(value1);
    this.isSamePass = (value1 === value2) && (value1.length !== 0 && value2.length !== 0);
  }

  passValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;

      if (!value) {
        return {passwordEmpty: true};
      }

      const isLengthValid = (value.length >= 6 && value.length <= 20);
      const hasUppercase = /[A-Z]/.test(value);
      const hasNumber = /[0-9]/.test(value);
      const hasSpecialCharacter = /[@#$%&*]/.test(value);

      const passwordValid = isLengthValid && hasUppercase && hasNumber && hasSpecialCharacter;

      return !passwordValid ? {
        passwordStrength: {
          isLengthValid,
          hasUppercase,
          hasNumber,
          hasSpecialCharacter
        }
      } : null;
    };
  }

  passMatchValidator(form: FormGroup): ValidationErrors | null {
    const contrasena1 = form.get('contrasena')?.value;
    const contrasena2 = form.get('confirmacionContrasena')?.value;

    return contrasena1 === contrasena2 ? null : {passwordsMismatch: true};
  }

  checkValidContrasena(): boolean {
    if (this.bedelForm.get('contrasena')?.value === this.bedelForm.get('confirmacionContrasena')?.value) {
      return true;
    }
    this.alertService.ok('ERROR', 'Las contraseñas no coinciden.').subscribe();
    return false;
  }

  checkValidPass(): boolean {
    if (this.isLengthValid && this.hasUppercase && this.hasNumber && this.hasSpecialCharacter) {
      return true;
    }
    this.alertService.ok('ERROR', 'Las contraseñas no son validas.').subscribe();
    return false;
  }

  clear(): void{
    if (this.bedelForm.touched){
      this.bedelForm.reset();
      this.bedelForm.clearValidators();
      this.bedelForm.updateValueAndValidity();
      this.bedelForm.setErrors(null);
    }
  }

  submit(): void {
    const checkValidPass = this.checkValidPass();
    const checkIncoincidentes = this.checkValidContrasena();

    if (!checkValidPass || !checkIncoincidentes) {
      return
    }

    this.alertService.confirm('Registrar', 'Desea registrar el bedel?').subscribe(() => {

      this.http.post<any>('http://localhost:8080/api/bedeles', this.bedelForm.value).subscribe({
          error: (value) => {
            if (value.status === 400 && value.error) {
              let errorMessages = '';

              if (typeof value.error === 'object') {
                for (const [field, message] of Object.entries(value.error)) {
                  errorMessages += `${message}.\n`;
                }
              }
              this.alertService.ok('ERROR', errorMessages);
              this.bedelForm.get('idRegistro')?.reset();
            }
          },
          complete: () => {
            this.alertService.snackBar('Bedel insertado correctamente.');
            this.clear();
          }
        }
      )
    });
  }
}
