jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICarta, Carta } from '../carta.model';
import { CartaService } from '../service/carta.service';

import { CartaRoutingResolveService } from './carta-routing-resolve.service';

describe('Service Tests', () => {
  describe('Carta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CartaRoutingResolveService;
    let service: CartaService;
    let resultCarta: ICarta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CartaRoutingResolveService);
      service = TestBed.inject(CartaService);
      resultCarta = undefined;
    });

    describe('resolve', () => {
      it('should return ICarta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCarta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCarta).toEqual({ id: 123 });
      });

      it('should return new ICarta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCarta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCarta).toEqual(new Carta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCarta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCarta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
