import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {FormBuilder, UntypedFormGroup} from "@angular/forms";
import {HttpClient, HttpParams} from "@angular/common/http";
import {AlertService} from "../../../services/alert/alert.service";
import {Select} from "../../../interfaces/select";
import {Router} from "@angular/router";

@Component({
  selector: 'app-buscar-bedel',
  templateUrl: './buscar-bedel.component.html',
  styleUrls: ['./buscar-bedel.component.scss']
})
export class BuscarBedelComponent implements OnInit{

  displayedColumns: string[] = ['apellido', 'nombre', 'tipoTurno', 'idRegistro', 'options'];
  turnos: Select[] = [{id: 0, name: 'Mañana'}, {id: 1, name: 'Tarde'}, {id: 2, name: 'Noche'}];
  dataSource = new MatTableDataSource();
  buscarBedelForm: UntypedFormGroup;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
    private alertService: AlertService,
    private router: Router,
  ) {
    this.buscarBedelForm = this.formBuilder.group({
      apellido: [null],
      tipoTurno: [null],
    })
  }

  ngOnInit() {
    this.submit();
  }

  editarBedel(row: any): void{
    this.router.navigate([`editar-bedel`, row.idRegistro]);
  }

  eliminarBedel(row: any): void{
    this.alertService.confirm('Eliminar', 'Desea eliminar el bedel?').subscribe(() => {
      this.http.delete(`http://localhost:8080/api/bedeles/${row.idRegistro}`).subscribe({
          next: ()=>{},
          complete: ()=> {
            this.submit();
          }
        }
      )
    })
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

    const queryParams = this.toQueryParams(this.buscarBedelForm.value);

    this.http.get(`http://localhost:8080/api/bedeles?${queryParams}`).subscribe({
        next: (value: any) => {
          if(value){
            this.dataSource = new MatTableDataSource(value);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          }else{
            this.dataSource = new MatTableDataSource();
          }

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
