import * as dayjs from 'dayjs';
import { ICarta } from 'app/entities/carta/carta.model';

export interface IJugador {
  id?: number;
  apodo?: string | null;
  nombre?: string | null;
  apellido?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  cartas?: ICarta[] | null;
}

export class Jugador implements IJugador {
  constructor(
    public id?: number,
    public apodo?: string | null,
    public nombre?: string | null,
    public apellido?: string | null,
    public fechaNacimiento?: dayjs.Dayjs | null,
    public cartas?: ICarta[] | null
  ) {}
}

export function getJugadorIdentifier(jugador: IJugador): number | undefined {
  return jugador.id;
}
