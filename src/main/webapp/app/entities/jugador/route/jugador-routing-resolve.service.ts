import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJugador, Jugador } from '../jugador.model';
import { JugadorService } from '../service/jugador.service';

@Injectable({ providedIn: 'root' })
export class JugadorRoutingResolveService implements Resolve<IJugador> {
  constructor(protected service: JugadorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJugador> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((jugador: HttpResponse<Jugador>) => {
          if (jugador.body) {
            return of(jugador.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Jugador());
  }
}
