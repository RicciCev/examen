import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJugador } from '../jugador.model';

@Component({
  selector: 'jhi-jugador-detail',
  templateUrl: './jugador-detail.component.html',
})
export class JugadorDetailComponent implements OnInit {
  jugador: IJugador | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jugador }) => {
      this.jugador = jugador;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
