import {Component} from '@angular/core';
import {FormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Turnos} from "../../../interfaces/turnos";
import {AlertService} from "../../../services/alert.service";

@Component({
  selector: 'app-registrar-bedel',
  templateUrl: './registrar-bedel.component.html',
  styleUrls: ['./registrar-bedel.component.scss']
})
export class RegistrarBedelComponent {

  bedelForm: UntypedFormGroup;
  turnos: Turnos[] = [{id: 0, name: 'Mañana'}, {id: 1, name: 'Tarde'}, {id: 3, name: 'Noche'}];

  constructor(
    private formbuilder: FormBuilder,
    private router: Router,
    private alertService: AlertService
  ) {
    this.bedelForm = this.formbuilder.group({
      id: [null, Validators.required],
      nombre: [null, Validators.required],
      apellido: [null, Validators.required],
      turno: [null, Validators.required],
      contrasena1: [null, Validators.required],
      contrasena2: [null, Validators.required],
    })
  }

  redirect(url: string) {
    this.router.navigate([url]);
  }

  close(): void {
    this.alertService.confirm('Cancelar', 'Desea cancelar el registro de bedel?').subscribe(() => {
      this.redirect('');
    });
  }

  checkValidId(): boolean {
    return true;
  }

  checkValidPolitics(): boolean {
    return true;
  }

  checkValidContrasena(): boolean {
    const contrasena1 = this.bedelForm.get('contrasena1')?.value;
    const contrasena2 = this.bedelForm.get('contrasena2')?.value;

    if (contrasena1 === contrasena2) {
      return true;
    }
    this.alertService.ok('ERROR', 'Las contraseñas no coinciden ').subscribe();
    return false;
  }


  submit(): void {
    const checkId = this.checkValidId();
    const checkPolitics = this.checkValidPolitics();
    const checkIncoincidentes = this.checkValidContrasena();

    if (!checkId || !checkPolitics || !checkIncoincidentes) {
      return
    }

    this.alertService.confirm('Registrar', 'Desea registrar el bedel?').subscribe(() => {
      console.log(this.bedelForm.value)
    });

  }
}
