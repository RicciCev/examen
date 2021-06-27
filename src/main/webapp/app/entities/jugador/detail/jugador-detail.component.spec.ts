import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JugadorDetailComponent } from './jugador-detail.component';

describe('Component Tests', () => {
  describe('Jugador Management Detail Component', () => {
    let comp: JugadorDetailComponent;
    let fixture: ComponentFixture<JugadorDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [JugadorDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ jugador: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(JugadorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(JugadorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load jugador on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.jugador).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
