import {Select} from "./select";

export interface Dia{
  id: number,
  name: string,
  value: boolean,
  hora: Select | null,
  duracion: Select | null,
}
