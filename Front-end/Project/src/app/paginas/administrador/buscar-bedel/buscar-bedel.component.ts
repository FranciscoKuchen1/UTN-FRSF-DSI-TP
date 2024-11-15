import {Component, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {FormBuilder, UntypedFormGroup} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {AlertService} from "../../../services/alert/alert.service";
import {Turnos} from "../../../interfaces/turnos";

@Component({
  selector: 'app-buscar-bedel',
  templateUrl: './buscar-bedel.component.html',
  styleUrls: ['./buscar-bedel.component.scss']
})
export class BuscarBedelComponent {

  displayedColumns: string[] = ['apellido', 'nombre', 'tipoTurno', 'idRegistro', 'options'];
  turnos: Turnos[] = [{id: 0, name: 'Mañana'}, {id: 1, name: 'Tarde'}, {id: 2, name: 'Noche'}];
  dataSource = new MatTableDataSource();
  buscarBedelForm: UntypedFormGroup;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private alertService: AlertService,
  ) {
    this.buscarBedelForm = this.formBuilder.group({
      apellido: [null],
      turno: [null],
    })
  }

  editarBedel(row: any): void{
    console.log('edito: ', row)
  }

  eliminarBedel(row: any): void{
    console.log('elimino: ', row)
  }

  submit(): void {
    this.http.get('http://localhost:8080/api/bedeles', this.buscarBedelForm.value).subscribe({
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
    )
  }


}
