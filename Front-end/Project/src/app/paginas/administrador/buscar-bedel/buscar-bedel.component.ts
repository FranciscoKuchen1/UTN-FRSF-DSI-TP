import {Component, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {FormBuilder, UntypedFormGroup} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {AlertService} from "../../../services/alert/alert.service";

@Component({
  selector: 'app-buscar-bedel',
  templateUrl: './buscar-bedel.component.html',
  styleUrls: ['./buscar-bedel.component.scss']
})
export class BuscarBedelComponent {

  displayedColumns: string[] = ['Apellido', 'Nombre', 'Turno', 'Identificador'];
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

  submit(): void {
    this.http.post<any>('http://localhost:8080/api/bedeles/buscar', this.buscarBedelForm.value).subscribe({
        next: (value) => {
          this.dataSource = new MatTableDataSource(value);
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
