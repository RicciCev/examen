import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CartaComponent } from './list/carta.component';
import { CartaDetailComponent } from './detail/carta-detail.component';
import { CartaUpdateComponent } from './update/carta-update.component';
import { CartaDeleteDialogComponent } from './delete/carta-delete-dialog.component';
import { CartaRoutingModule } from './route/carta-routing.module';

@NgModule({
  imports: [SharedModule, CartaRoutingModule],
  declarations: [CartaComponent, CartaDetailComponent, CartaUpdateComponent, CartaDeleteDialogComponent],
  entryComponents: [CartaDeleteDialogComponent],
})
export class CartaModule {}
