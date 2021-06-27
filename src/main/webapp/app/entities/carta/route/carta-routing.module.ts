import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CartaComponent } from '../list/carta.component';
import { CartaDetailComponent } from '../detail/carta-detail.component';
import { CartaUpdateComponent } from '../update/carta-update.component';
import { CartaRoutingResolveService } from './carta-routing-resolve.service';

const cartaRoute: Routes = [
  {
    path: '',
    component: CartaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CartaDetailComponent,
    resolve: {
      carta: CartaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CartaUpdateComponent,
    resolve: {
      carta: CartaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CartaUpdateComponent,
    resolve: {
      carta: CartaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cartaRoute)],
  exports: [RouterModule],
})
export class CartaRoutingModule {}
