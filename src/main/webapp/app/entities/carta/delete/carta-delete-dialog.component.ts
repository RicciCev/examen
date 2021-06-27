import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarta } from '../carta.model';
import { CartaService } from '../service/carta.service';

@Component({
  templateUrl: './carta-delete-dialog.component.html',
})
export class CartaDeleteDialogComponent {
  carta?: ICarta;

  constructor(protected cartaService: CartaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cartaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
