import {Component, Inject} from '@angular/core';
import {NestedTreeControl} from "@angular/cdk/tree";
import {MatTreeNestedDataSource} from "@angular/material/tree";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

interface ReservaSolapada {
  nombreCatedra: string;
  nombreDocente: string;
  apellidoDocente: string;
  correoDocente: string;
  inicioReserva: string;
  finReserva: string;
}

interface AulaConSolapamiento {
  aula: {
    numero: number;
    nombre: string;
    piso: number;
    capacidad: number;
  };
  reservaSolapada: ReservaSolapada;
}

interface DiaConSolapamiento {
  diaReservado: {
    fechaReserva: string;
    horaInicio: string;
    duracion: number;
  };
  aulasConSolapamiento: AulaConSolapamiento[];
}

interface TreeNode {
  name: string;
  children?: TreeNode[];
  reservaSolapada?: ReservaSolapada;
  aula?: {
    numero: number;
    nombre: string;
    piso: number;
    capacidad: number;
  };
}

@Component({
  selector: 'app-registrar-reserva-dialog',
  templateUrl: './registrar-reserva-dialog.component.html',
  styleUrls: ['./registrar-reserva-dialog.component.scss']
})
export class RegistrarReservaDialogComponent{
  treeControl = new NestedTreeControl<TreeNode>(node => node.children);
  dataSource = new MatTreeNestedDataSource<TreeNode>();

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    this.initializeTreeData();
  }

  private initializeTreeData(): void {
    if (this.data && this.data.diasConSolapamiento) {
      const treeData: TreeNode[] = this.data.diasConSolapamiento.map((dia: DiaConSolapamiento) => ({
        name: dia.diaReservado.fechaReserva,
        children: dia.aulasConSolapamiento.map((aula: AulaConSolapamiento) => ({
          name: `${aula.aula.nombre} (${aula.aula.numero})`,
          reservaSolapada: aula.reservaSolapada,
          aula: aula.aula
        }))
      }));
      this.dataSource.data = treeData;
    } else {
      console.error('Los datos proporcionados no tienen el formato esperado');
      this.dataSource.data = [];
    }
  }

  hasChild = (_: number, node: TreeNode) => !!node.children && node.children.length > 0;

  isReservaSolapada = (_: number, node: TreeNode) => !!node.reservaSolapada;
}
