import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { JugadorComponent } from './list/jugador.component';
import { JugadorDetailComponent } from './detail/jugador-detail.component';
import { JugadorUpdateComponent } from './update/jugador-update.component';
import { JugadorDeleteDialogComponent } from './delete/jugador-delete-dialog.component';
import { JugadorRoutingModule } from './route/jugador-routing.module';

@NgModule({
  imports: [SharedModule, JugadorRoutingModule],
  declarations: [JugadorComponent, JugadorDetailComponent, JugadorUpdateComponent, JugadorDeleteDialogComponent],
  entryComponents: [JugadorDeleteDialogComponent],
})
export class JugadorModule {}
