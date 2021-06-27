import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICarta } from '../carta.model';

@Component({
  selector: 'jhi-carta-detail',
  templateUrl: './carta-detail.component.html',
})
export class CartaDetailComponent implements OnInit {
  carta: ICarta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carta }) => {
      this.carta = carta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
