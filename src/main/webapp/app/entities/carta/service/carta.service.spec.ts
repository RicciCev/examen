import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICarta, Carta } from '../carta.model';

import { CartaService } from './carta.service';

describe('Service Tests', () => {
  describe('Carta Service', () => {
    let service: CartaService;
    let httpMock: HttpTestingController;
    let elemDefault: ICarta;
    let expectedResult: ICarta | ICarta[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CartaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        tipo: 'AAAAAAA',
        costeMana: 0,
        texto: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Carta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Carta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Carta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            tipo: 'BBBBBB',
            costeMana: 1,
            texto: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Carta', () => {
        const patchObject = Object.assign(
          {
            tipo: 'BBBBBB',
          },
          new Carta()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Carta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            tipo: 'BBBBBB',
            costeMana: 1,
            texto: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Carta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCartaToCollectionIfMissing', () => {
        it('should add a Carta to an empty array', () => {
          const carta: ICarta = { id: 123 };
          expectedResult = service.addCartaToCollectionIfMissing([], carta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(carta);
        });

        it('should not add a Carta to an array that contains it', () => {
          const carta: ICarta = { id: 123 };
          const cartaCollection: ICarta[] = [
            {
              ...carta,
            },
            { id: 456 },
          ];
          expectedResult = service.addCartaToCollectionIfMissing(cartaCollection, carta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Carta to an array that doesn't contain it", () => {
          const carta: ICarta = { id: 123 };
          const cartaCollection: ICarta[] = [{ id: 456 }];
          expectedResult = service.addCartaToCollectionIfMissing(cartaCollection, carta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(carta);
        });

        it('should add only unique Carta to an array', () => {
          const cartaArray: ICarta[] = [{ id: 123 }, { id: 456 }, { id: 9527 }];
          const cartaCollection: ICarta[] = [{ id: 123 }];
          expectedResult = service.addCartaToCollectionIfMissing(cartaCollection, ...cartaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const carta: ICarta = { id: 123 };
          const carta2: ICarta = { id: 456 };
          expectedResult = service.addCartaToCollectionIfMissing([], carta, carta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(carta);
          expect(expectedResult).toContain(carta2);
        });

        it('should accept null and undefined values', () => {
          const carta: ICarta = { id: 123 };
          expectedResult = service.addCartaToCollectionIfMissing([], null, carta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(carta);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
