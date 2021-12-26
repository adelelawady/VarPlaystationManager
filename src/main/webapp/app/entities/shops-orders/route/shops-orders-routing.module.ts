import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShopsOrdersComponent } from '../list/shops-orders.component';
import { ShopsOrdersDetailComponent } from '../detail/shops-orders-detail.component';
import { ShopsOrdersUpdateComponent } from '../update/shops-orders-update.component';
import { ShopsOrdersRoutingResolveService } from './shops-orders-routing-resolve.service';

const shopsOrdersRoute: Routes = [
  {
    path: '',
    component: ShopsOrdersComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShopsOrdersDetailComponent,
    resolve: {
      shopsOrders: ShopsOrdersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShopsOrdersUpdateComponent,
    resolve: {
      shopsOrders: ShopsOrdersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShopsOrdersUpdateComponent,
    resolve: {
      shopsOrders: ShopsOrdersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(shopsOrdersRoute)],
  exports: [RouterModule],
})
export class ShopsOrdersRoutingModule {}
