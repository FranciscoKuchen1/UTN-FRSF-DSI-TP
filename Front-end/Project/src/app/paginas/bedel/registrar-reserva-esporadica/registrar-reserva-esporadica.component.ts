import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {FormBuilder, UntypedFormGroup, Validators} from "@angular/forms";
import {Select} from "../../../interfaces/select";
import {Aula} from "../../../interfaces/aula";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AlertService} from "../../../services/alert/alert.service";
import {Router} from "@angular/router";
import {Esporadica} from "../../../interfaces/esporadica";
import {Docente} from "../../../interfaces/docente";
import {MAT_DATE_FORMATS, MAT_DATE_LOCALE} from "@angular/material/core";
import {MatStepper} from "@angular/material/stepper";

export const MY_DATE_FORMATS = {
  parse: {
    dateInput: 'dd/MM/yyyy',
  },
  display: {
    dateInput: 'dd/MM/yyyy',
    monthYearLabel: 'MMM yyyy',
    dateA11yLabel: 'dd/MM/yyyy',
    monthYearA11yLabel: 'MMMM yyyy',
  },
};

@Component({
  selector: 'app-registrar-reserva-esporadica',
  templateUrl: './registrar-reserva-esporadica.component.html',
  styleUrls: ['./registrar-reserva-esporadica.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    { provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMATS },
    { provide: MAT_DATE_LOCALE, useValue: 'es-ES' }
  ],
})
export class RegistrarReservaEsporadicaComponent implements OnInit{

  @ViewChild(MatStepper) stepper: MatStepper;

  registrarReservaForm: UntypedFormGroup;
  registrarAulasForm: UntypedFormGroup;

  periodos: Select[] = [{id: 3, name: 'Esporadico'}];
  nombreCatedras: Select[] = [{id: 1, name: 'Análisis Numérico'}, {id: 2, name: 'Física II'}, {id: 3, name: 'Probabilidad y Estadística'}];
  tipoAulas: Select[] = [{id: 0, name: 'Aula sin recursos adicionales'}, {id: 1, name: 'Aula informatica'}, {id: 2, name: 'Multimedios'}];
  docentes: Docente[] = [
    {id: 1, nombre: 'Juan', apellido: 'Perez' , correo: 'jperez@gmail.com'},
    {id: 2, nombre: 'María', apellido: 'Gomez', correo: 'gomezmaria88@outlook.com'},
    {id: 3, nombre: 'Carlos', apellido: 'Lopez', correo: 'carloslopezk@hotmail.com'}
  ];
  horas: Select[] = [
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
  tiposPizarrones: Select[] = [{id: 0, name: 'Pizarron verde'}, {id: 1, name: 'Pizarron blanco'}];

  disabledAgregar: boolean = false;
  disableRegistrar: boolean = false;

  opcionesAulas: Aula[] = [];

  fechasDisponibles: any[];
  fechaSeleccionada: any;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private alertService: AlertService,
    private router: Router,
  ) {
    this.registrarReservaForm = this.formBuilder.group({
      idRegistroBedel: ['Clara'],
      tipoPeriodo: [3, Validators.required],
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

      fechaAReservar: [null],
      horaInicioFechaAReservar: [null],
      duracionFechaAReservar: [null],
      diasReservadosDTO: [[], Validators.required],
    })

    this.registrarAulasForm = this.formBuilder.group({
      fechaSeleccionada: [],
      aulaSeleccionada: [],
    });

    this.registrarReservaForm.get('tipoPeriodo')?.disable();
  }

  ngOnInit() {

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

    this.registrarReservaForm.valueChanges.subscribe({
      next: (value)=> {
        this.disabledAgregar = value.fechaAReservar && (value.horaInicioFechaAReservar !== null) && (value.duracionFechaAReservar !== null);
      }
    })

    this.registrarAulasForm.get('fechaSeleccionada')?.valueChanges.subscribe({
      next: (value)=> {
        if(value){
          this.fechaSeleccionada = value[0];

          if(this.fechaSeleccionada.diaReservado.idAula){
            this.registrarAulasForm.get('aulaSeleccionada')?.patchValue([this.fechaSeleccionada.diaReservado.idAula]);
          }else{
            this.registrarAulasForm.get('aulaSeleccionada')?.reset();
          }

          this.opcionesAulas = this.fechaSeleccionada.aulasDisponibles;
        }
      }
    });

    this.registrarAulasForm.get('aulaSeleccionada')?.valueChanges.subscribe({
      next: (value)=> {
        if(value){
          this.fechaSeleccionada.diaReservado.idAula = value[0];
          this.verificarDisabled();
        }
      }
    });
  }

  verificarDisabled(): void{
    this.disableRegistrar = this.fechasDisponibles.every(data=> {return data.diaReservado.idAula});
  }

  formatDateToISO(date: Date): string {
    const options: Intl.DateTimeFormatOptions = {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    };

    return date.toLocaleDateString('en-CA', options);
  }

  agregarALista(): void{
    const fecha = this.registrarReservaForm.get('fechaAReservar')?.value;
    const hora = this.registrarReservaForm.get('horaInicioFechaAReservar')?.value;
    const duracion = this.duraciones[this.registrarReservaForm.get('duracionFechaAReservar')?.value].name;

    const fechasGuardadas = this.registrarReservaForm.get('diasReservadosDTO')?.value;

    fechasGuardadas.push({fechaReserva: this.formatDateToISO(fecha), horaInicio: hora, duracion: parseInt(duracion)});

    this.registrarReservaForm.get('diasReservadosDTO')?.patchValue(fechasGuardadas);

    this.registrarReservaForm.get('fechaAReservar')?.reset();
    this.registrarReservaForm.get('horaInicioFechaAReservar')?.reset();
    this.registrarReservaForm.get('duracionFechaAReservar')?.reset();
  }

  borrarDeLista(fecha : Esporadica){
    const fechas= this.registrarReservaForm.get('diasReservadosDTO')?.value;
    const nuevasFechas = fechas.filter((value: Esporadica) => { return value !== fecha});

    this.registrarReservaForm.get('diasReservadosDTO')?.patchValue(nuevasFechas);
  }

  redirect(url: string) {
    this.router.navigate([url]);
  }


  close(): void{
    this.alertService.confirm('Cancelar', 'Desea cancelar el registro de reserva?').subscribe(() => {
      this.redirect('');
    });
  }

  listaCaracteristicas(aula: Aula): string{

    const tipoAula = this.tipoAulas[aula.tipoAula].name ?? null;
    const aireAcondicionado = aula.tieneAireAcondicionado ? 'Aire acondicionado' : null;
    const tipoPizarron = this.tiposPizarrones[aula.tipoPizarron]?.name ?? null;

    let result = [];

    if (tipoAula) result.push(tipoAula);
    if (aireAcondicionado) result.push(aireAcondicionado);
    if (tipoPizarron) result.push(tipoPizarron);

    return result.join(', ');
  }

  retrocederStep(): void{
    this.registrarAulasForm.get('fechaSeleccionada')?.reset()
    this.registrarAulasForm.get('aulaSeleccionada')?.reset()
    this.fechaSeleccionada = null;
    this.disableRegistrar = false;
  }

  siguiente(): void{

    const tempCantAlumnos = this.registrarReservaForm.get('cantAlumnos')?.value;
    this.registrarReservaForm.get('cantAlumnos')?.patchValue(parseInt(tempCantAlumnos));

    //this.registrarReservaForm.removeControl('docente');
    //this.registrarReservaForm.removeControl('catedra');
    //this.registrarReservaForm.removeControl('fechaAReservar');
    //this.registrarReservaForm.removeControl('horaInicioFechaAReservar');
    //this.registrarReservaForm.removeControl('duracionFechaAReservar');


    this.http.post<any>('http://localhost:8080/api/reservasEsporadicas/disponibilidad', this.registrarReservaForm.getRawValue()).subscribe({
      next: (data)=> {
        if(data.diasDisponibles.length !== 0 && data.diasConSolapamiento.length === 0){
          this.fechasDisponibles = data.diasDisponibles;
          this.stepper.next();

        }else{
          //todo cartel que devuelva errores
          console.log('CARTEL DE ERROR PARA FECHAS NO DISPONIBLESSSSS');
        }
      },
      error: (value) => {
        console.log('error: ', value)
      }
    });

  }

  submit(): void{

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    this.http.post<any>('http://localhost:8080/api/reservasEsporadicas', this.fechasDisponibles, {headers}).subscribe({
      next: ()=> {

      },
      error: (value) => {
        console.log('error: ', value)
      },
      complete: ()=> {
        this.alertService.snackBar('Reserva realizada con exito.');
        this.redirect('');
      }
    });

  }

}
