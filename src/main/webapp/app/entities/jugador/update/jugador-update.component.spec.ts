jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { JugadorService } from '../service/jugador.service';
import { IJugador, Jugador } from '../jugador.model';
import { ICarta } from 'app/entities/carta/carta.model';
import { CartaService } from 'app/entities/carta/service/carta.service';

import { JugadorUpdateComponent } from './jugador-update.component';

describe('Component Tests', () => {
  describe('Jugador Management Update Component', () => {
    let comp: JugadorUpdateComponent;
    let fixture: ComponentFixture<JugadorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let jugadorService: JugadorService;
    let cartaService: CartaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JugadorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(JugadorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JugadorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      jugadorService = TestBed.inject(JugadorService);
      cartaService = TestBed.inject(CartaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Carta query and add missing value', () => {
        const jugador: IJugador = { id: 456 };
        const cartas: ICarta[] = [{ id: 65094 }];
        jugador.cartas = cartas;

        const cartaCollection: ICarta[] = [{ id: 67723 }];
        spyOn(cartaService, 'query').and.returnValue(of(new HttpResponse({ body: cartaCollection })));
        const additionalCartas = [...cartas];
        const expectedCollection: ICarta[] = [...additionalCartas, ...cartaCollection];
        spyOn(cartaService, 'addCartaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ jugador });
        comp.ngOnInit();

        expect(cartaService.query).toHaveBeenCalled();
        expect(cartaService.addCartaToCollectionIfMissing).toHaveBeenCalledWith(cartaCollection, ...additionalCartas);
        expect(comp.cartasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const jugador: IJugador = { id: 456 };
        const cartas: ICarta = { id: 40258 };
        jugador.cartas = [cartas];

        activatedRoute.data = of({ jugador });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(jugador));
        expect(comp.cartasSharedCollection).toContain(cartas);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const jugador = { id: 123 };
        spyOn(jugadorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ jugador });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: jugador }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(jugadorService.update).toHaveBeenCalledWith(jugador);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const jugador = new Jugador();
        spyOn(jugadorService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ jugador });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: jugador }));
        saveSubject.complete();

        // THEN
        expect(jugadorService.create).toHaveBeenCalledWith(jugador);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const jugador = { id: 123 };
        spyOn(jugadorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ jugador });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(jugadorService.update).toHaveBeenCalledWith(jugador);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCartaById', () => {
        it('Should return tracked Carta primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCartaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedCarta', () => {
        it('Should return option if no Carta is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedCarta(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Carta for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedCarta(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Carta is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedCarta(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
