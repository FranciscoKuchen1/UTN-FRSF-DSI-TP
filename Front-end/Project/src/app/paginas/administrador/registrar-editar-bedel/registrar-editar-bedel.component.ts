import {Component, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  UntypedFormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {Select} from "../../../interfaces/select";
import {AlertService} from "../../../services/alert/alert.service";
import {HttpClient} from "@angular/common/http";
import {CanComponentDeactivate, CanDeactivateType} from "../../../guards/canDeactivate/can-deactivate.guard";
import {Subject} from "rxjs";

@Component({
  selector: 'app-registrar-editar-bedel',
  templateUrl: './registrar-editar-bedel.component.html',
  styleUrls: ['./registrar-editar-bedel.component.scss']
})
export class RegistrarEditarBedelComponent implements OnInit, CanComponentDeactivate{

  bedelForm: UntypedFormGroup;
  turnos: Select[] = [{id: 0, name: 'Mañana'}, {id: 1, name: 'Tarde'}, {id: 2, name: 'Noche'}];
  id: string | null = null;
  formChanged: boolean;

  isLengthValid = false;
  isSamePass = false;
  hasUppercase = false;
  hasNumber = false;
  hasSpecialCharacter = false;

  extraData: any;

  constructor(
    private formbuilder: FormBuilder,
    private router: Router,
    private alertService: AlertService,
    private http: HttpClient,
    private route: ActivatedRoute,
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

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.id = params.get('id');
      this.extraData = history.state.extraData;
    });

    if (this.id) {
      this.http.get(`http://localhost:8080/api/bedeles/${this.id}`).subscribe({
        next: (value: any) => {
          this.bedelForm.patchValue(value);
          this.bedelForm.get('idRegistro')?.disable();

          this.isLengthValid = true;
          this.hasUppercase = true;
          this.hasNumber = true;
          this.hasSpecialCharacter = true;
        },
        error: () => {
          this.alertService.ok('ERROR', 'Error en la edicion de bedel.').subscribe();
          this.redirect('buscar-bedel');
        }
      })
    }

    this.bedelForm.valueChanges.subscribe((value)=>{
      this.formChanged = !!value;
    })
  }

  canDeactivate(): CanDeactivateType {
    if(this.formChanged) {
      const deactivateSubject = new Subject<boolean>();
      this.alertService.confirm('Cancelar', 'Desea cancelar el registro de bedel?').subscribe((status) => {
        if(status){
          deactivateSubject.next(true);
        }else{
          deactivateSubject.next(false);
        }
      });
      return deactivateSubject;
    } else {
      return true;
    }
  }

  redirect(url: string, extraData?: any) {
    this.router.navigate([url], {
      state: { extraData: extraData }
    });
  }

  close(): void {
    this.formChanged = false;
    if(this.id){
      this.alertService.confirm('Cancelar', 'Desea cancelar la edición del bedel?').subscribe(() => {
        this.redirect('buscar-bedel', this.extraData);
      });
    }else{
      this.alertService.confirm('Cancelar', 'Desea cancelar el registro de bedel?').subscribe(() => {
        this.redirect('');
      });
    }
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

  clear(): void {
    this.bedelForm.setErrors(null);
    this.bedelForm.clearValidators();
    this.bedelForm.updateValueAndValidity();
    this.bedelForm.reset();
  }

  submit(): void {
    const checkValidPass = this.checkValidPass();
    const checkIncoincidentes = this.checkValidContrasena();

    if (!checkValidPass || !checkIncoincidentes) {
      return
    }

    if(this.id){

      this.alertService.confirm('Editar', 'Desea editar el bedel?').subscribe(() => {
        this.bedelForm.get('idRegistro')?.patchValue(this.id);

        this.http.put<any>(`http://localhost:8080/api/bedeles/${this.id}`, this.bedelForm.getRawValue()).subscribe({
            error: (value) => {
              if (value.status === 400 && value.error) {
                let errorMessages = '';

                if (typeof value.error === 'object') {
                  for (const [field, message] of Object.entries(value.error)) {
                    errorMessages += `${message}\n`;
                  }
                }
                this.alertService.ok('ERROR', errorMessages);
              }
            },
            complete: () => {
              this.alertService.snackBar('Bedel editado correctamente.');
              this.formChanged = false;
              this.clear();
              this.redirect('buscar-bedel', this.extraData);
            }
          }
        )
      });

    }else{

      this.alertService.confirm('Registrar', 'Desea registrar el bedel?').subscribe(() => {

        this.http.post<any>('http://localhost:8080/api/bedeles', this.bedelForm.value).subscribe({
            error: (value) => {
              if (value.status === 400 && value.error) {
                let errorMessages = '';

                if (typeof value.error === 'object') {
                  for (const [field, message] of Object.entries(value.error)) {
                    errorMessages += `${message}\n`;
                  }
                }
                this.alertService.ok('ERROR', errorMessages);
                this.bedelForm.get('idRegistro')?.reset();
              }
            },
            complete: () => {
              this.alertService.snackBar('Bedel insertado correctamente.');
              this.formChanged = false;
              this.clear();
            }
          }
        )
      });
    }
  }
}
