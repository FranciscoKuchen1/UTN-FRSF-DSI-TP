import {Turnos} from "./turnos";

export interface Bedel {
  idRegistro: string,
  nombre: string,
  apellido: string,
  tipoTurno: Turnos,
  contrasena: string,
  confirmacionContrasena: string,

}
