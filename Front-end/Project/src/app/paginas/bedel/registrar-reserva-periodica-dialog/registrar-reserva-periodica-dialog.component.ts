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

interface DiasSemanaConSolapamiento {
  diaSemana: {
    dia: number;
    horaInicio: string;
    duracion: number;
  };
  aulasConSolapamiento: AulaConSolapamiento[];
}

interface TreeNode {
  name: string[];
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
  selector: 'app-registrar-reserva-periodica-dialog',
  templateUrl: './registrar-reserva-periodica-dialog.component.html',
  styleUrls: ['./registrar-reserva-periodica-dialog.component.scss']
})
export class RegistrarReservaPeriodicaDialogComponent {

  treeControl = new NestedTreeControl<TreeNode>(node => node.children);
  dataSource = new MatTreeNestedDataSource<TreeNode>();

  dias: any[] = [
    {dia: 0, name: 'Domingo'},
    {dia: 1, name: 'Lunes'},
    {dia: 2, name: 'Martes'},
    {dia: 3, name: 'Miercole'},
    {dia: 4, name: 'Jueves'},
    {dia: 5, name: 'Viernes'},
    {dia: 6, name: 'Sabado'},
  ]

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    this.initializeTreeData();
  }

  private initializeTreeData(): void {
    if (this.data && this.data.diasSemanaConSolapamiento) {
      const treeData: TreeNode[] = this.data.diasSemanaConSolapamiento.map((dia: DiasSemanaConSolapamiento) => ({
        name: [this.dias[dia.diaSemana.dia].name, this.transformarHoraInicioFin(dia.diaSemana.horaInicio, dia.diaSemana.duracion)],
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

  transformarHoraInicioFin(horaInicio: string, duracionMinutos: number): string{

    const [horas, minutos] = horaInicio.split(':').map(Number);
    const totalMinutos = horas * 60 + minutos;
    const nuevaHoraEnMinutos = totalMinutos + parseInt(duracionMinutos.toString());
    const nuevaHora = Math.floor(nuevaHoraEnMinutos / 60) % 24;
    const nuevosMinutos = nuevaHoraEnMinutos % 60;
    const nuevaHoraFormateada = `${String(nuevaHora).padStart(2, '0')}:${String(nuevosMinutos).padStart(2, '0')}`;

    return ` de ${horaInicio.substring(0,5)} a ${nuevaHoraFormateada}` ?? '';

  }
}
