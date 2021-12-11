import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecordComponent } from '../list/record.component';
import { RecordDetailComponent } from '../detail/record-detail.component';
import { RecordUpdateComponent } from '../update/record-update.component';
import { RecordRoutingResolveService } from './record-routing-resolve.service';

const recordRoute: Routes = [
  {
    path: '',
    component: RecordComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecordDetailComponent,
    resolve: {
      record: RecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecordUpdateComponent,
    resolve: {
      record: RecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecordUpdateComponent,
    resolve: {
      record: RecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recordRoute)],
  exports: [RouterModule],
})
export class RecordRoutingModule {}
