import { IJugador } from 'app/entities/jugador/jugador.model';

export interface ICarta {
  id?: number;
  nombre?: string | null;
  tipo?: string | null;
  costeMana?: number | null;
  texto?: string | null;
  jugadores?: IJugador[] | null;
}

export class Carta implements ICarta {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public tipo?: string | null,
    public costeMana?: number | null,
    public texto?: string | null,
    public jugadores?: IJugador[] | null
  ) {}
}

export function getCartaIdentifier(carta: ICarta): number | undefined {
  return carta.id;
}
