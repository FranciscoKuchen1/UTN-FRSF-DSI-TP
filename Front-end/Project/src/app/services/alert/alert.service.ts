import { Injectable } from '@angular/core';
import {filter, Observable} from "rxjs";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {AlertData} from "../../interfaces/alertData";
import {AlertComponent} from "../../components/alert/alert.component";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor(private dialog: MatDialog, private snackbar: MatSnackBar) {}

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

  snackBar(message: string, action: string = 'CERRAR'): void {
    this.snackbar.open(message, action, {
      duration: 5000,
      verticalPosition: 'top',
      panelClass: ['matSnackBarStyle']
    });
  }

}
