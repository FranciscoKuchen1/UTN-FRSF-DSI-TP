import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {Select} from "../../../interfaces/select";
import {Dia} from "../../../interfaces/dias";
import {Aula} from "../../../interfaces/aula";
import {Fecha} from "../../../interfaces/fecha";
import {HttpClient} from "@angular/common/http";
import {AlertService} from "../../../services/alert/alert.service";
import {Router} from "@angular/router";
import {Esporadica} from "../../../interfaces/esporadica";

@Component({
  selector: 'app-registrar-reserva-esporadica',
  templateUrl: './registrar-reserva-esporadica.component.html',
  styleUrls: ['./registrar-reserva-esporadica.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RegistrarReservaEsporadicaComponent implements OnInit{

  registrarReservaForm: UntypedFormGroup;
  registrarAulasForm: UntypedFormGroup;

  periodos: Select[] = [{id: 3, name: 'Esporadico'}];
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
    {id: 0, name: '30'},
    {id: 1, name: '60'},
    {id: 2, name: '90'},
    {id: 3, name: '120'},
  ];

  disabledAgregar: boolean = false;

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
      periodo: [null, Validators.required],
      nombreCatedra: [null, Validators.required],
      tipoAula: [null, Validators.required],
      cantidadAlumnos: [null, Validators.required],
      nombreDocente: [null, Validators.required],
      correoElectronico: [null, [Validators.required, Validators.email]],
      diasReservados: [null, Validators.required],
      fechaAReservar: [null],
      horaInicioFechaAReservar: [null],
      duracionFechaAReservar: [null],
      fechasReservadas: [[]],
    })

    this.registrarAulasForm = this.formBuilder.group({
      fechaSeleccionada: [],
      aulaSeleccionada: [],
    });
  }

  ngOnInit() {
    this.registrarReservaForm.valueChanges.subscribe({
      next: (value)=> {
        this.disabledAgregar = value.fechaAReservar && (value.horaInicioFechaAReservar !== null) && (value.duracionFechaAReservar !== null);
      }
    })
  }

  agregarALista(): void{
    const fecha = this.registrarReservaForm.get('fechaAReservar')?.value;
    const hora = this.horas[this.registrarReservaForm.get('horaInicioFechaAReservar')?.value];
    const duracion = this.duraciones[this.registrarReservaForm.get('duracionFechaAReservar')?.value];

    const fechasGuardadas = this.registrarReservaForm.get('fechasReservadas')?.value;

    fechasGuardadas.push({fecha: fecha, hora: hora, duracion: duracion});

    this.registrarReservaForm.get('fechasReservadas')?.patchValue(fechasGuardadas);

    this.registrarReservaForm.get('fechaAReservar')?.reset();
    this.registrarReservaForm.get('horaInicioFechaAReservar')?.reset();
    this.registrarReservaForm.get('duracionFechaAReservar')?.reset();
  }

  borrarDeLista(fecha : Esporadica){
    const fechas= this.registrarReservaForm.get('fechasReservadas')?.value;
    const nuevasFechas = fechas.filter((value: Esporadica) => { return value !== fecha});

    this.registrarReservaForm.get('fechasReservadas')?.patchValue(nuevasFechas);
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
