import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AlertData} from "../../interfaces/alertData";

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent {

  constructor(public dialog: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: AlertData) {}

  close(status: boolean): void {
    this.dialog.close(status);
  }
}
