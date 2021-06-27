import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICarta, Carta } from '../carta.model';
import { CartaService } from '../service/carta.service';

@Component({
  selector: 'jhi-carta-update',
  templateUrl: './carta-update.component.html',
})
export class CartaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, []],
    tipo: [],
    costeMana: [],
    texto: [],
  });

  constructor(protected cartaService: CartaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carta }) => {
      this.updateForm(carta);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const carta = this.createFromForm();
    if (carta.id !== undefined) {
      this.subscribeToSaveResponse(this.cartaService.update(carta));
    } else {
      this.subscribeToSaveResponse(this.cartaService.create(carta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarta>>): void {
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

  protected updateForm(carta: ICarta): void {
    this.editForm.patchValue({
      id: carta.id,
      nombre: carta.nombre,
      tipo: carta.tipo,
      costeMana: carta.costeMana,
      texto: carta.texto,
    });
  }

  protected createFromForm(): ICarta {
    return {
      ...new Carta(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      costeMana: this.editForm.get(['costeMana'])!.value,
      texto: this.editForm.get(['texto'])!.value,
    };
  }
}
