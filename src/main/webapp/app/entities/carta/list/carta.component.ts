import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICarta } from '../carta.model';
import { CartaService } from '../service/carta.service';
import { CartaDeleteDialogComponent } from '../delete/carta-delete-dialog.component';

@Component({
  selector: 'jhi-carta',
  templateUrl: './carta.component.html',
})
export class CartaComponent implements OnInit {
  cartas?: ICarta[];
  isLoading = false;

  constructor(protected cartaService: CartaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cartaService.query().subscribe(
      (res: HttpResponse<ICarta[]>) => {
        this.isLoading = false;
        this.cartas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICarta): number {
    return item.id!;
  }

  delete(carta: ICarta): void {
    const modalRef = this.modalService.open(CartaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.carta = carta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
