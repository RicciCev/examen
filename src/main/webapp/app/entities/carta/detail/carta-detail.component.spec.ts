import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CartaDetailComponent } from './carta-detail.component';

describe('Component Tests', () => {
  describe('Carta Management Detail Component', () => {
    let comp: CartaDetailComponent;
    let fixture: ComponentFixture<CartaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CartaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ carta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CartaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CartaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load carta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.carta).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
