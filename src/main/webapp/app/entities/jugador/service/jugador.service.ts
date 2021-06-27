import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJugador, getJugadorIdentifier } from '../jugador.model';

export type EntityResponseType = HttpResponse<IJugador>;
export type EntityArrayResponseType = HttpResponse<IJugador[]>;

@Injectable({ providedIn: 'root' })
export class JugadorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/jugadors');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(jugador: IJugador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jugador);
    return this.http
      .post<IJugador>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(jugador: IJugador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jugador);
    return this.http
      .put<IJugador>(`${this.resourceUrl}/${getJugadorIdentifier(jugador) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(jugador: IJugador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jugador);
    return this.http
      .patch<IJugador>(`${this.resourceUrl}/${getJugadorIdentifier(jugador) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IJugador>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IJugador[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addJugadorToCollectionIfMissing(jugadorCollection: IJugador[], ...jugadorsToCheck: (IJugador | null | undefined)[]): IJugador[] {
    const jugadors: IJugador[] = jugadorsToCheck.filter(isPresent);
    if (jugadors.length > 0) {
      const jugadorCollectionIdentifiers = jugadorCollection.map(jugadorItem => getJugadorIdentifier(jugadorItem)!);
      const jugadorsToAdd = jugadors.filter(jugadorItem => {
        const jugadorIdentifier = getJugadorIdentifier(jugadorItem);
        if (jugadorIdentifier == null || jugadorCollectionIdentifiers.includes(jugadorIdentifier)) {
          return false;
        }
        jugadorCollectionIdentifiers.push(jugadorIdentifier);
        return true;
      });
      return [...jugadorsToAdd, ...jugadorCollection];
    }
    return jugadorCollection;
  }

  protected convertDateFromClient(jugador: IJugador): IJugador {
    return Object.assign({}, jugador, {
      fechaNacimiento: jugador.fechaNacimiento?.isValid() ? jugador.fechaNacimiento.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaNacimiento = res.body.fechaNacimiento ? dayjs(res.body.fechaNacimiento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((jugador: IJugador) => {
        jugador.fechaNacimiento = jugador.fechaNacimiento ? dayjs(jugador.fechaNacimiento) : undefined;
      });
    }
    return res;
  }
}
