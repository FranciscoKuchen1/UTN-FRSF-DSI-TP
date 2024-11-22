import {Component, ViewChild} from '@angular/core';
import {Select} from "../../../interfaces/select";
import {MatTableDataSource} from "@angular/material/table";
import {FormBuilder, UntypedFormGroup} from "@angular/forms";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {HttpClient, HttpParams} from "@angular/common/http";
import {AlertService} from "../../../services/alert/alert.service";

@Component({
  selector: 'app-listado-reservas-curso',
  templateUrl: './listado-reservas-curso.component.html',
  styleUrls: ['./listado-reservas-curso.component.scss']
})
export class ListadoReservasCursoComponent {

  displayedColumns: string[] = ['Día de reserva', 'Hora de inicio', 'Duración', 'Aula'];
  dataSource = new MatTableDataSource();
  listadoReservaCursolForm: UntypedFormGroup;
  catedras: Select[] = [{id: 0, name: 'catedra1'},{id: 1, name: 'catedra2'},{id: 2, name: 'catedra3'},{id: 3, name: 'catedra4'}];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private alertService: AlertService,
  ) {
    this.listadoReservaCursolForm = this.formBuilder.group({
      nombreCatedra: [null],
      anio: [null],
    })
  }

  imprimir(): void{

  }

  toQueryParams(data: {[id: string]: string}): string {
    const filtered = {};

    for (const key in data) {
      if (data[key] !== null && data[key] !== undefined && data[key] !== '') {
        // @ts-ignore
        filtered[key] = data[key];
      }
    }
    return new HttpParams({fromObject: filtered}).toString();
  }

  submit(): void {

    const queryParams = this.toQueryParams(this.listadoReservaCursolForm.value);

    /*this.http.get(`http://localhost:8080/api/bedeles?${queryParams}`).subscribe({
        next: (value: any) => {
          this.dataSource = new MatTableDataSource(value);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (value) => {
          if (value.status === 400 && value.error) {
            let errorMessages = '';

            if (typeof value.error === 'object') {
              for (const [field, message] of Object.entries(value.error)) {
                errorMessages += `${message}.\n`;
              }
            }
            this.alertService.ok('ERROR', errorMessages);
          }
        }
      }
    )*/
  }
}
