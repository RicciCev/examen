import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CartaService } from '../service/carta.service';

import { CartaComponent } from './carta.component';

describe('Component Tests', () => {
  describe('Carta Management Component', () => {
    let comp: CartaComponent;
    let fixture: ComponentFixture<CartaComponent>;
    let service: CartaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CartaComponent],
      })
        .overrideTemplate(CartaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CartaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CartaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.cartas?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
