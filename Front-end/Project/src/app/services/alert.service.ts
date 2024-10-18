import { Injectable } from '@angular/core';
import {filter, Observable} from "rxjs";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {AlertData} from "../interfaces/alertData";
import {AlertComponent} from "../components/alert/alert.component";

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor(private dialog: MatDialog) {}

  show(alertData: AlertData): MatDialogRef<AlertComponent> {
    return this.dialog.open(AlertComponent, {
      width: '400px',
      data: alertData,
      disableClose: true
    });
  }

  confirm(
    titulo: string,
    descripcion: string,
    positive: string = 'SÍ',
    negative: string = 'NO'
  ): Observable<boolean> {
    const dialog = this.show({
      title: titulo,
      text: descripcion,
      positiveString: positive,
      negativeString: negative,
    });

    return dialog.afterClosed().pipe(filter((val) => val));
  }

  ok(titulo: string, descripcion: string): Observable<undefined> {
    const dialog = this.show({
      title: titulo,
      text: descripcion,
      positiveString: 'CERRAR',
    });

    return dialog.afterClosed();
  }

}
