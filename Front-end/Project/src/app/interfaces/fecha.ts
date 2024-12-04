import {Aula} from "./aula";

export interface Fecha{
  fecha: string,
  aulasDisponibles: Aula[],
  aulaSeleccionada: Aula | null,
}
