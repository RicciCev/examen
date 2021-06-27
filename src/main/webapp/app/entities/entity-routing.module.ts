import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'jugador',
        data: { pageTitle: 'Jugadors' },
        loadChildren: () => import('./jugador/jugador.module').then(m => m.JugadorModule),
      },
      {
        path: 'carta',
        data: { pageTitle: 'Cartas' },
        loadChildren: () => import('./carta/carta.module').then(m => m.CartaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
