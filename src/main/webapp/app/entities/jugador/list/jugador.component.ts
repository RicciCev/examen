import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IJugador } from '../jugador.model';
import { JugadorService } from '../service/jugador.service';
import { JugadorDeleteDialogComponent } from '../delete/jugador-delete-dialog.component';

@Component({
  selector: 'jhi-jugador',
  templateUrl: './jugador.component.html',
})
export class JugadorComponent implements OnInit {
  jugadors?: IJugador[];
  isLoading = false;

  constructor(protected jugadorService: JugadorService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.jugadorService.query().subscribe(
      (res: HttpResponse<IJugador[]>) => {
        this.isLoading = false;
        this.jugadors = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IJugador): number {
    return item.id!;
  }

  delete(jugador: IJugador): void {
    const modalRef = this.modalService.open(JugadorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.jugador = jugador;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
