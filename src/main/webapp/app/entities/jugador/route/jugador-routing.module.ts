import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { JugadorComponent } from '../list/jugador.component';
import { JugadorDetailComponent } from '../detail/jugador-detail.component';
import { JugadorUpdateComponent } from '../update/jugador-update.component';
import { JugadorRoutingResolveService } from './jugador-routing-resolve.service';

const jugadorRoute: Routes = [
  {
    path: '',
    component: JugadorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JugadorDetailComponent,
    resolve: {
      jugador: JugadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JugadorUpdateComponent,
    resolve: {
      jugador: JugadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JugadorUpdateComponent,
    resolve: {
      jugador: JugadorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(jugadorRoute)],
  exports: [RouterModule],
})
export class JugadorRoutingModule {}
