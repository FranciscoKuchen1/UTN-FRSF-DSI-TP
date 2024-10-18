import {Turnos} from "./turnos";

export interface Bedel {
  id: string,
  nombre: string,
  apellido: string,
  turno: Turnos,
  contrasena1: string,
  contrasena2: string,

}
