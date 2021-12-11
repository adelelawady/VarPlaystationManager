import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SessionComponent } from '../list/session.component';
import { SessionDetailComponent } from '../detail/session-detail.component';
import { SessionUpdateComponent } from '../update/session-update.component';
import { SessionRoutingResolveService } from './session-routing-resolve.service';

const sessionRoute: Routes = [
  {
    path: '',
    component: SessionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SessionDetailComponent,
    resolve: {
      session: SessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SessionUpdateComponent,
    resolve: {
      session: SessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SessionUpdateComponent,
    resolve: {
      session: SessionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sessionRoute)],
  exports: [RouterModule],
})
export class SessionRoutingModule {}
