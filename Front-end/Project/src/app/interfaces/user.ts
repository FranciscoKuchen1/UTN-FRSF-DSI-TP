import {NameId} from "./name-id";

export interface User {
  user?: string;
  pass?: string;
  nombre?: string;
  apellido?: string;
  rol?: NameId;
}
