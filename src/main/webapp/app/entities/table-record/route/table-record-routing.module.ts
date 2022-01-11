import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TableRecordComponent } from '../list/table-record.component';
import { TableRecordDetailComponent } from '../detail/table-record-detail.component';
import { TableRecordRoutingResolveService } from './table-record-routing-resolve.service';
import { TableRecordShopsComponent } from '../list-shops/table-record.component';
import { TableRecordTakeawayComponent } from '../list-takeaway/table-record.component';

const tableRecordRoute: Routes = [
  {
    path: 'table',
    component: TableRecordComponent,
    data: {
      defaultSort: 'id,asc',
    },
    resolve: {
      tableType: TableRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'takeaway',
    component: TableRecordTakeawayComponent,
    data: {
      defaultSort: 'id,asc',
    },
    resolve: {
      tableType: TableRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'shops',
    component: TableRecordShopsComponent,
    data: {
      defaultSort: 'id,asc',
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
