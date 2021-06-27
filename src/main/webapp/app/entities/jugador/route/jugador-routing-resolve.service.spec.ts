jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IJugador, Jugador } from '../jugador.model';
import { JugadorService } from '../service/jugador.service';

import { JugadorRoutingResolveService } from './jugador-routing-resolve.service';

describe('Service Tests', () => {
  describe('Jugador routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: JugadorRoutingResolveService;
    let service: JugadorService;
    let resultJugador: IJugador | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(JugadorRoutingResolveService);
      service = TestBed.inject(JugadorService);
      resultJugador = undefined;
    });

    describe('resolve', () => {
      it('should return IJugador returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJugador = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultJugador).toEqual({ id: 123 });
      });

      it('should return new IJugador if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJugador = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultJugador).toEqual(new Jugador());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJugador = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultJugador).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
