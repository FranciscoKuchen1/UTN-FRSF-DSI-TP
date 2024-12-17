export interface Aula{
  nombre: string,
  capacidad: string,
  caracteristicas: string,
  piso: number,
  numero: number,
  tieneAireAcondicionado: boolean,
  tipoAula: number,
  tipoPizarron: number,
  atributosEspecificos: Atributos,

}

interface Atributos{
  tieneVentiladores: boolean,

  cantidadPCs: number,
  tieneCanon: boolean,

  tieneTelevisor: boolean,
  tieneComputadora: boolean,
}
