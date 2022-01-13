import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SheftComponent } from '../list/sheft.component';
import { SheftDetailComponent } from '../detail/sheft-detail.component';
import { SheftRoutingResolveService } from './sheft-routing-resolve.service';

const sheftRoute: Routes = [
  {
    path: '',
    component: SheftComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SheftDetailComponent,
    resolve: {
      sheft: SheftRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sheftRoute)],
  exports: [RouterModule],
})
export class SheftRoutingModule {}
