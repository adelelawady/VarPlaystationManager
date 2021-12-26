import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TakeawayComponent } from '../list/takeaway.component';
import { TakeawayDetailComponent } from '../detail/takeaway-detail.component';
import { TakeawayUpdateComponent } from '../update/takeaway-update.component';
import { TakeawayRoutingResolveService } from './takeaway-routing-resolve.service';

const takeawayRoute: Routes = [
  {
    path: '',
    component: TakeawayComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TakeawayDetailComponent,
    resolve: {
      takeaway: TakeawayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TakeawayUpdateComponent,
    resolve: {
      takeaway: TakeawayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TakeawayUpdateComponent,
    resolve: {
      takeaway: TakeawayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(takeawayRoute)],
  exports: [RouterModule],
})
export class TakeawayRoutingModule {}
