import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {AlertService} from "../../../services/alert/alert.service";
import {Router} from "@angular/router";
import {Select} from "../../../interfaces/select";
import {Dia} from "../../../interfaces/dias";

@Component({
  selector: 'app-registrar-reserva-periodica',
  templateUrl: './registrar-reserva-periodica.component.html',
  styleUrls: ['./registrar-reserva-periodica.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class RegistrarReservaPeriodicaComponent{

  registrarReservaForm: UntypedFormGroup;
  registrarAulasForm: UntypedFormGroup;

  periodos: Select[] = [{id: 0, name: 'Primer cuatrimestre'}, {id: 1, name: 'Segundo cuatrimestre'}, {id: 2, name: 'Anual'}];
  nombreCatedras: Select[] = [{id: 0, name: 'Catedra 1'}, {id: 1, name: 'Catedra 2'}, {id: 2, name: 'Catedra 3'}];
  tipoAulas: Select[] = [{id: 0, name: 'Multimedios'}, {id: 1, name: 'Aula informatica'}, {id: 2, name: 'Aula sin recursos adicionales'}];
  nombreDocentes: Select[] = [{id: 0, name: 'Docente 1'}, {id: 1, name: 'Docente 2'}, {id: 2, name: 'Docente 3'}];
  horas: Select[] = [
    {id: 0, name: '00:00'},
    {id: 1, name: '00:30'},
    {id: 2, name: '01:00'},
    {id: 3, name: '01:30'},
    {id: 4, name: '02:00'},
    {id: 5, name: '02:30'},
    {id: 6, name: '03:00'},
    {id: 7, name: '03:30'},
    {id: 8, name: '04:00'},
    {id: 9, name: '04:30'},
    {id: 10, name: '05:00'},
    {id: 11, name: '05:30'},
    {id: 12, name: '06:00'},
    {id: 13, name: '06:30'},
    {id: 14, name: '07:00'},
    {id: 15, name: '07:30'},
    {id: 16, name: '08:00'},
    {id: 17, name: '08:30'},
    {id: 18, name: '09:00'},
    {id: 19, name: '09:30'},
    {id: 20, name: '10:00'},
    {id: 21, name: '10:30'},
    {id: 22, name: '11:00'},
    {id: 23, name: '11:30'},
    {id: 24, name: '12:00'},
    {id: 25, name: '12:30'},
    {id: 26, name: '13:00'},
    {id: 27, name: '13:30'},
    {id: 28, name: '14:00'},
    {id: 29, name: '14:30'},
    {id: 30, name: '15:00'},
    {id: 31, name: '15:30'},
    {id: 32, name: '16:00'},
    {id: 33, name: '16:30'},
    {id: 34, name: '17:00'},
    {id: 35, name: '17:30'},
    {id: 36, name: '18:00'},
    {id: 37, name: '18:30'},
    {id: 38, name: '19:00'},
    {id: 39, name: '19:30'},
    {id: 40, name: '20:00'},
    {id: 41, name: '20:30'},
    {id: 42, name: '21:00'},
    {id: 43, name: '21:30'},
    {id: 44, name: '22:00'},
    {id: 45, name: '22:30'},
    {id: 46, name: '23:00'},
    {id: 47, name: '23:30'},
  ];
  duraciones: Select[] = [
    {id: 0, name: '30 Mins'},
    {id: 1, name: '60 Mins'},
    {id: 2, name: '90 Mins'},
    {id: 3, name: '120 Mins'},
  ];
  dias: Dia[] = [
    {id: 0, name: 'Domingo', value: false, hora: null, duracion: null},
    {id: 1, name: 'Lunes', value: false, hora: null, duracion: null},
    {id: 2, name: 'Martes', value: false, hora: null, duracion: null},
    {id: 3, name: 'Miercoles', value: false, hora: null, duracion: null},
    {id: 4, name: 'Jueves', value: false, hora: null, duracion: null},
    {id: 5, name: 'Viernes', value: false, hora: null, duracion: null},
    {id: 6, name: 'Sabado', value: false, hora: null, duracion: null}];

  todosValidos: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private alertService: AlertService,
    private router: Router,
  ) {
    this.registrarReservaForm = this.formBuilder.group({
      periodo: [null, Validators.required],
      nombreCatedra: [null, Validators.required],
      tipoAula: [null, Validators.required],
      cantidadAlumnos: [null, Validators.required],
      nombreDocente: [null, Validators.required],
      correoElectronico: [null, [Validators.required, Validators.email]],
      diasReservados: [null, Validators.required],
    })

    this.registrarAulasForm = this.formBuilder.group({});
  }

  diasCompletos(): boolean{
    const diasActivados = this.dias.filter(data => data.value);

    if(diasActivados.length > 0){
      return diasActivados.every(data => data.hora && data.duracion);
    }else{
      return false;
    }

  }

  changeCheckbox(diaCheck: Dia): void{
   this.dias.find(dia => {
      if(dia === diaCheck){
        dia.value = !dia.value;
      }
    });

    this.registrarReservaForm.get('diasReservados')?.patchValue(this.dias.filter(data=> data.value));
    this.todosValidos = this.diasCompletos();
  }

  asignarHora(dia: Dia, hora: Select, $event: any): void{
    if($event.isUserInput){
      dia.hora = hora;

      const dias = this.dias.filter(value => value.value && value.duracion)
      this.registrarReservaForm.get('diasReservados')?.patchValue(dias);
      this.todosValidos = this.diasCompletos();

    }
  }
  asignarDuracion(dia: Dia, duracion: Select, $event: any): void{
    if($event.isUserInput){
      dia.duracion = duracion;

      const dias = this.dias.filter(value => value.value && value.hora)
      this.registrarReservaForm.get('diasReservados')?.patchValue(dias);
      this.todosValidos = this.diasCompletos();
    }
  }

  redirect(url: string) {
    this.router.navigate([url]);
  }

  close(): void{
    this.alertService.confirm('Cancelar', 'Desea cancelar el registro de reserva?').subscribe(() => {
      this.redirect('');
    });
  }

  siguiente(): void{

    console.log('dias seleccionados: ', this.registrarReservaForm.value)

  }

  submit(): void{

  }
}
