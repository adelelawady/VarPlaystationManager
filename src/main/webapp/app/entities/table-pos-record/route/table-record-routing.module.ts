import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TableRecordComponent } from '../list/table-record.component';
import { TableRecordDetailComponent } from '../detail/table-record-detail.component';
import { TableRecordRoutingResolveService } from './table-record-routing-resolve.service';

const tableRecordRoute: Routes = [
  {
    path: 'cafe',
    component: TableRecordComponent,
    data: {
      defaultSort: 'id,asc',
      type: 'cafe',
    },
    resolve: {
      tableType: TableRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'res',
    component: TableRecordComponent,
    data: {
      defaultSort: 'id,asc',
      type: 'res',
    },
    resolve: {
      tableType: TableRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'market',
    component: TableRecordComponent,
    data: {
      defaultSort: 'id,asc',
      type: 'market',
    },
    resolve: {
      tableType: TableRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TableRecordDetailComponent,
    resolve: {
      tableRecord: TableRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tableRecordRoute)],
  exports: [RouterModule],
})
export class TableRecordRoutingModule {}
