import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICarta, Carta } from '../carta.model';
import { CartaService } from '../service/carta.service';

@Injectable({ providedIn: 'root' })
export class CartaRoutingResolveService implements Resolve<ICarta> {
  constructor(protected service: CartaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICarta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((carta: HttpResponse<Carta>) => {
          if (carta.body) {
            return of(carta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Carta());
  }
}
