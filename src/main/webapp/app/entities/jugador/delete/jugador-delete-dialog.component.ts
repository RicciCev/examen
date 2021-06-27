import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IJugador } from '../jugador.model';
import { JugadorService } from '../service/jugador.service';

@Component({
  templateUrl: './jugador-delete-dialog.component.html',
})
export class JugadorDeleteDialogComponent {
  jugador?: IJugador;

  constructor(protected jugadorService: JugadorService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jugadorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
