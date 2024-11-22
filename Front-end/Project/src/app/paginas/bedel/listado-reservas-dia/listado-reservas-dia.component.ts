import {Component, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {FormBuilder, UntypedFormGroup} from "@angular/forms";
import {Select} from "../../../interfaces/select";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {HttpClient, HttpParams} from "@angular/common/http";
import {AlertService} from "../../../services/alert/alert.service";

@Component({
  selector: 'app-listado-reservas-dia',
  templateUrl: './listado-reservas-dia.component.html',
  styleUrls: ['./listado-reservas-dia.component.scss']
})
export class ListadoReservasDiaComponent {
  displayedColumns: string[] = ['docente', 'catedra', 'tipoAula', 'ocupacion','hora inicio', 'hora fin'];
  dataSource = new MatTableDataSource();
  listadoReservaDiaForm: UntypedFormGroup;
  tiposAula: Select[] = [{id: 0, name: 'Todas'},{id: 1, name: 'Multimedios'},{id: 2, name: 'Aula informatica'},{id: 3, name: 'Aula sin recursos adicionales'}];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private alertService: AlertService,
  ) {
    this.listadoReservaDiaForm = this.formBuilder.group({
      fecha: [null],
      tipoAula: [null],
      aulas: [null],
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

    const queryParams = this.toQueryParams(this.listadoReservaDiaForm.value);

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
