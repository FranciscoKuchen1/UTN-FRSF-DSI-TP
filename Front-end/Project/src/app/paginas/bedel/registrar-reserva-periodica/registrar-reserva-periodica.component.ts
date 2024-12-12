import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {AlertService} from "../../../services/alert/alert.service";
import {Router} from "@angular/router";
import {Select} from "../../../interfaces/select";
import {Dia} from "../../../interfaces/dias";
import {Aula} from "../../../interfaces/aula";
import {Fecha} from "../../../interfaces/fecha";
import {Docente} from "../../../interfaces/docente";

@Component({
  selector: 'app-registrar-reserva-periodica',
  templateUrl: './registrar-reserva-periodica.component.html',
  styleUrls: ['./registrar-reserva-periodica.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class RegistrarReservaPeriodicaComponent implements OnInit{

  registrarReservaForm: UntypedFormGroup;
  registrarAulasForm: UntypedFormGroup;

  periodos: Select[] = [{id: 0, name: 'Primer cuatrimestre'}, {id: 1, name: 'Segundo cuatrimestre'}, {id: 2, name: 'Anual'}];
  nombreCatedras: Select[] = [{id: 1, name: 'Análisis Numérico'}, {id: 2, name: 'Física II'}, {id: 3, name: 'Probabilidad y Estadística'}];
  tipoAulas: Select[] = [{id: 0, name: 'Aula sin recursos adicionales'}, {id: 1, name: 'Aula informatica'}, {id: 2, name: 'Multimedios'}];
  docentes: Docente[] = [
    {id: 1, nombre: 'Juan', apellido: 'Perez' , correo: 'jperez@gmail.com'},
    {id: 2, nombre: 'María', apellido: 'Gomez', correo: 'gomezmaria88@outlook.com'},
    {id: 3, nombre: 'Carlos', apellido: 'Lopez', correo: 'carloslopezk@hotmail.com'}
  ];
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
    {id: 0, name: '30'},
    {id: 1, name: '60'},
    {id: 2, name: '90'},
    {id: 3, name: '120'},
    {id: 4, name: '150'},
    {id: 5, name: '180'},
    {id: 6, name: '210'},
    {id: 7, name: '240'},
  ];
  dias: Dia[] = [
    {id: 7, name: 'Domingo', value: false, hora: null, duracion: null},
    {id: 1, name: 'Lunes', value: false, hora: null, duracion: null},
    {id: 2, name: 'Martes', value: false, hora: null, duracion: null},
    {id: 3, name: 'Miercoles', value: false, hora: null, duracion: null},
    {id: 4, name: 'Jueves', value: false, hora: null, duracion: null},
    {id: 5, name: 'Viernes', value: false, hora: null, duracion: null},
    {id: 6, name: 'Sabado', value: false, hora: null, duracion: null}];
  todosValidos: boolean = false;

  fechaSeleccionada: string;
  aulasDisponiblesPorFecha: Aula[];

  opcionesAulas: Aula[] = [
    {
      nombre: 'Nombre aula 1',
      ubicacion: 'Ubicacion aula 1',
      capacidad: 'Capacidad aula 1',
      caracteristicas: 'Caracteristicas aula 1'
    },
    {
      nombre: 'Nombre aula 2',
      ubicacion: 'Ubicacion aula 2',
      capacidad: 'Capacidad aula 2',
      caracteristicas: 'Caracteristicas aula 2'
    },
    {
      nombre: 'Nombre aula 3',
      ubicacion: 'Ubicacion aula 3',
      capacidad: 'Capacidad aula 3',
      caracteristicas: 'Caracteristicas aula 3'
    }
  ];

  opcionesAulas2: Aula[] = [
    {
      nombre: 'Nombre aula 12',
      ubicacion: 'Ubicacion aula 12',
      capacidad: 'Capacidad aula 12',
      caracteristicas: 'Caracteristicas aula 12'
    },
    {
      nombre: 'Nombre aula 22',
      ubicacion: 'Ubicacion aula 22',
      capacidad: 'Capacidad aula 22',
      caracteristicas: 'Caracteristicas aula 22'
    },
    {
      nombre: 'Nombre aula 32',
      ubicacion: 'Ubicacion aula 32',
      capacidad: 'Capacidad aula 32',
      caracteristicas: 'Caracteristicas aula 32'
    }
  ];


  fechasDisponibles: Fecha[] = [
    {fecha: '12/12/12' , aulasDisponibles: this.opcionesAulas, aulaSeleccionada: null},
    {fecha: '13/12/12' , aulasDisponibles: this.opcionesAulas2, aulaSeleccionada: null},
    {fecha: '14/12/12' , aulasDisponibles: this.opcionesAulas, aulaSeleccionada: null},
    {fecha: '15/12/12' , aulasDisponibles: this.opcionesAulas2, aulaSeleccionada: null},
    {fecha: '16/12/12' , aulasDisponibles: this.opcionesAulas, aulaSeleccionada: null},
    {fecha: '17/12/12' , aulasDisponibles: this.opcionesAulas2, aulaSeleccionada: null},
    {fecha: '18/12/12' , aulasDisponibles: this.opcionesAulas, aulaSeleccionada: null},
    {fecha: '19/12/12' , aulasDisponibles: this.opcionesAulas2, aulaSeleccionada: null},
    {fecha: '20/12/12' , aulasDisponibles: this.opcionesAulas, aulaSeleccionada: null},
    {fecha: '21/12/12' , aulasDisponibles: this.opcionesAulas2, aulaSeleccionada: null},
    {fecha: '22/12/12' , aulasDisponibles: this.opcionesAulas, aulaSeleccionada: null},
    {fecha: '23/12/12' , aulasDisponibles: this.opcionesAulas2, aulaSeleccionada: null},
    {fecha: '24/12/12' , aulasDisponibles: this.opcionesAulas, aulaSeleccionada: null},
  ];

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private alertService: AlertService,
    private router: Router,
  ) {
    this.registrarReservaForm = this.formBuilder.group({
      idRegistroBedel: [1],
      tipoPeriodo: [null, Validators.required],
      catedra: [null, Validators.required],
      idCatedra: [null],
      nombreCatedra: [null],
      tipoAula: [null, Validators.required],
      cantAlumnos: [null, Validators.required],
      docente: [null, Validators.required],
      idDocente: [null],
      nombreDocente: [null],
      apellidoDocente: [null],
      correoDocente: [null, [Validators.required, Validators.email]],
      diasSemanaHorasDuracion: [null, Validators.required],
    })

    this.registrarAulasForm = this.formBuilder.group({
      fechaSeleccionada: [],
      aulaSeleccionada: [],
    });
  }

  ngOnInit() {
    /*this.registrarAulasForm.get('fechaSeleccionada')?.valueChanges.subscribe({
      next: (value)=> {
        if(value){
          this.fechaSeleccionada = value;
          const [aulasDisponibles] = this.fechasDisponibles.filter(data => data.fecha === value);
          this.aulasDisponiblesPorFecha = aulasDisponibles.aulasDisponibles;
        }
      }
    });*/

    this.registrarReservaForm.get('docente')?.valueChanges.subscribe({
      next: (value)=> {
        if(value){
         this.registrarReservaForm.get('idDocente')?.patchValue(value.id);
         this.registrarReservaForm.get('nombreDocente')?.patchValue(value.nombre);
         this.registrarReservaForm.get('apellidoDocente')?.patchValue(value.apellido);
         this.registrarReservaForm.get('correoDocente')?.patchValue(value.correo);
        }else{
          this.registrarReservaForm.get('idDocente')?.reset();
          this.registrarReservaForm.get('nombreDocente')?.reset();
          this.registrarReservaForm.get('apellidoDocente')?.reset();
          this.registrarReservaForm.get('correoDocente')?.reset();
        }
      }
    });

    this.registrarReservaForm.get('catedra')?.valueChanges.subscribe({
      next: (value)=> {
        if(value){
          this.registrarReservaForm.get('idCatedra')?.patchValue(value.id);
          this.registrarReservaForm.get('nombreCatedra')?.patchValue(value.name);
        }else{
          this.registrarReservaForm.get('idCatedra')?.reset();
          this.registrarReservaForm.get('nombreCatedra')?.reset();
        }
      }
    });
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

    this.registrarReservaForm.get('diasSemanaHorasDuracion')?.patchValue(this.dias.filter(data=> data.value));
    this.todosValidos = this.diasCompletos();
  }

  asignarHora(dia: Dia, hora: Select, $event: any): void{
    if($event.isUserInput){
      dia.hora = hora.name;

      const dias = this.dias.filter(value => value.value && value.duracion)
      this.registrarReservaForm.get('diasSemanaHorasDuracion')?.patchValue(dias);
      this.todosValidos = this.diasCompletos();

    }
  }

  asignarDuracion(dia: Dia, duracion: Select, $event: any): void{
    if($event.isUserInput){
      dia.duracion = duracion.name;

      const dias = this.dias.filter(value => value.value && value.hora)
      this.registrarReservaForm.get('diasSemanaHorasDuracion')?.patchValue(dias);
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
    const temp = this.registrarReservaForm.get('diasSemanaHorasDuracion')?.value;

    const newArr = temp.map((obj: Dia) => {
      const { value, name, ...rest } = obj;
      return rest;
    });

    this.registrarReservaForm.get('diasSemanaHorasDuracion')?.patchValue(newArr);

    this.http.post<any>('http://localhost:8080/api/reservasPeriodicas/disponibilidad', this.registrarReservaForm.value).subscribe({
        error: (value) => {
          console.log('error: ', value)
        },
        complete: () => {
          console.log('entro bien')
        }
      });
  }

  submit(): void{

  }
}
