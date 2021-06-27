import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { JugadorService } from '../service/jugador.service';

import { JugadorComponent } from './jugador.component';

describe('Component Tests', () => {
  describe('Jugador Management Component', () => {
    let comp: JugadorComponent;
    let fixture: ComponentFixture<JugadorComponent>;
    let service: JugadorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [JugadorComponent],
      })
        .overrideTemplate(JugadorComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(JugadorComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(JugadorService);

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
      expect(comp.jugadors?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
