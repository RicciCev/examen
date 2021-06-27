import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IJugador, Jugador } from '../jugador.model';
import { JugadorService } from '../service/jugador.service';
import { ICarta } from 'app/entities/carta/carta.model';
import { CartaService } from 'app/entities/carta/service/carta.service';

@Component({
  selector: 'jhi-jugador-update',
  templateUrl: './jugador-update.component.html',
})
export class JugadorUpdateComponent implements OnInit {
  isSaving = false;

  cartasSharedCollection: ICarta[] = [];

  editForm = this.fb.group({
    id: [],
    apodo: [null, [Validators.pattern('^[a-zA-Z0-9_]*$')]],
    nombre: [],
    apellido: [],
    fechaNacimiento: [],
    cartas: [],
  });

  constructor(
    protected jugadorService: JugadorService,
    protected cartaService: CartaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jugador }) => {
      this.updateForm(jugador);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jugador = this.createFromForm();
    if (jugador.id !== undefined) {
      this.subscribeToSaveResponse(this.jugadorService.update(jugador));
    } else {
      this.subscribeToSaveResponse(this.jugadorService.create(jugador));
    }
  }

  trackCartaById(index: number, item: ICarta): number {
    return item.id!;
  }

  getSelectedCarta(option: ICarta, selectedVals?: ICarta[]): ICarta {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJugador>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(jugador: IJugador): void {
    this.editForm.patchValue({
      id: jugador.id,
      apodo: jugador.apodo,
      nombre: jugador.nombre,
      apellido: jugador.apellido,
      fechaNacimiento: jugador.fechaNacimiento,
      cartas: jugador.cartas,
    });

    this.cartasSharedCollection = this.cartaService.addCartaToCollectionIfMissing(this.cartasSharedCollection, ...(jugador.cartas ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.cartaService
      .query()
      .pipe(map((res: HttpResponse<ICarta[]>) => res.body ?? []))
      .pipe(
        map((cartas: ICarta[]) => this.cartaService.addCartaToCollectionIfMissing(cartas, ...(this.editForm.get('cartas')!.value ?? [])))
      )
      .subscribe((cartas: ICarta[]) => (this.cartasSharedCollection = cartas));
  }

  protected createFromForm(): IJugador {
    return {
      ...new Jugador(),
      id: this.editForm.get(['id'])!.value,
      apodo: this.editForm.get(['apodo'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value,
      cartas: this.editForm.get(['cartas'])!.value,
    };
  }
}
