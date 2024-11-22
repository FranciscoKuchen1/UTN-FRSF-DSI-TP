import {Select} from "./select";

export interface Bedel {
  idRegistro: string,
  nombre: string,
  apellido: string,
  tipoTurno: Select,
  contrasena: string,
  confirmacionContrasena: string,

}
