jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CartaService } from '../service/carta.service';
import { ICarta, Carta } from '../carta.model';

import { CartaUpdateComponent } from './carta-update.component';

describe('Component Tests', () => {
  describe('Carta Management Update Component', () => {
    let comp: CartaUpdateComponent;
    let fixture: ComponentFixture<CartaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cartaService: CartaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CartaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CartaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CartaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cartaService = TestBed.inject(CartaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const carta: ICarta = { id: 456 };

        activatedRoute.data = of({ carta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(carta));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const carta = { id: 123 };
        spyOn(cartaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ carta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: carta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cartaService.update).toHaveBeenCalledWith(carta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const carta = new Carta();
        spyOn(cartaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ carta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: carta }));
        saveSubject.complete();

        // THEN
        expect(cartaService.create).toHaveBeenCalledWith(carta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const carta = { id: 123 };
        spyOn(cartaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ carta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cartaService.update).toHaveBeenCalledWith(carta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
