import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICarta, getCartaIdentifier } from '../carta.model';

export type EntityResponseType = HttpResponse<ICarta>;
export type EntityArrayResponseType = HttpResponse<ICarta[]>;

@Injectable({ providedIn: 'root' })
export class CartaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/cartas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(carta: ICarta): Observable<EntityResponseType> {
    return this.http.post<ICarta>(this.resourceUrl, carta, { observe: 'response' });
  }

  update(carta: ICarta): Observable<EntityResponseType> {
    return this.http.put<ICarta>(`${this.resourceUrl}/${getCartaIdentifier(carta) as number}`, carta, { observe: 'response' });
  }

  partialUpdate(carta: ICarta): Observable<EntityResponseType> {
    return this.http.patch<ICarta>(`${this.resourceUrl}/${getCartaIdentifier(carta) as number}`, carta, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICarta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICarta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCartaToCollectionIfMissing(cartaCollection: ICarta[], ...cartasToCheck: (ICarta | null | undefined)[]): ICarta[] {
    const cartas: ICarta[] = cartasToCheck.filter(isPresent);
    if (cartas.length > 0) {
      const cartaCollectionIdentifiers = cartaCollection.map(cartaItem => getCartaIdentifier(cartaItem)!);
      const cartasToAdd = cartas.filter(cartaItem => {
        const cartaIdentifier = getCartaIdentifier(cartaItem);
        if (cartaIdentifier == null || cartaCollectionIdentifiers.includes(cartaIdentifier)) {
          return false;
        }
        cartaCollectionIdentifiers.push(cartaIdentifier);
        return true;
      });
      return [...cartasToAdd, ...cartaCollection];
    }
    return cartaCollection;
  }
}
