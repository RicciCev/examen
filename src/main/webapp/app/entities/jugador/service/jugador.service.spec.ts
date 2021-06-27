import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IJugador, Jugador } from '../jugador.model';

import { JugadorService } from './jugador.service';

describe('Service Tests', () => {
  describe('Jugador Service', () => {
    let service: JugadorService;
    let httpMock: HttpTestingController;
    let elemDefault: IJugador;
    let expectedResult: IJugador | IJugador[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(JugadorService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        apodo: 'AAAAAAA',
        nombre: 'AAAAAAA',
        apellido: 'AAAAAAA',
        fechaNacimiento: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaNacimiento: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Jugador', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaNacimiento: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );

        service.create(new Jugador()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Jugador', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            apodo: 'BBBBBB',
            nombre: 'BBBBBB',
            apellido: 'BBBBBB',
            fechaNacimiento: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Jugador', () => {
        const patchObject = Object.assign(
          {
            apodo: 'BBBBBB',
            nombre: 'BBBBBB',
            fechaNacimiento: currentDate.format(DATE_FORMAT),
          },
          new Jugador()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Jugador', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            apodo: 'BBBBBB',
            nombre: 'BBBBBB',
            apellido: 'BBBBBB',
            fechaNacimiento: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Jugador', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addJugadorToCollectionIfMissing', () => {
        it('should add a Jugador to an empty array', () => {
          const jugador: IJugador = { id: 123 };
          expectedResult = service.addJugadorToCollectionIfMissing([], jugador);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(jugador);
        });

        it('should not add a Jugador to an array that contains it', () => {
          const jugador: IJugador = { id: 123 };
          const jugadorCollection: IJugador[] = [
            {
              ...jugador,
            },
            { id: 456 },
          ];
          expectedResult = service.addJugadorToCollectionIfMissing(jugadorCollection, jugador);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Jugador to an array that doesn't contain it", () => {
          const jugador: IJugador = { id: 123 };
          const jugadorCollection: IJugador[] = [{ id: 456 }];
          expectedResult = service.addJugadorToCollectionIfMissing(jugadorCollection, jugador);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(jugador);
        });

        it('should add only unique Jugador to an array', () => {
          const jugadorArray: IJugador[] = [{ id: 123 }, { id: 456 }, { id: 67978 }];
          const jugadorCollection: IJugador[] = [{ id: 123 }];
          expectedResult = service.addJugadorToCollectionIfMissing(jugadorCollection, ...jugadorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const jugador: IJugador = { id: 123 };
          const jugador2: IJugador = { id: 456 };
          expectedResult = service.addJugadorToCollectionIfMissing([], jugador, jugador2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(jugador);
          expect(expectedResult).toContain(jugador2);
        });

        it('should accept null and undefined values', () => {
          const jugador: IJugador = { id: 123 };
          expectedResult = service.addJugadorToCollectionIfMissing([], null, jugador, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(jugador);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
