import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeviceTypeComponent } from '../list/device-type.component';
import { DeviceTypeDetailComponent } from '../detail/device-type-detail.component';
import { DeviceTypeUpdateComponent } from '../update/device-type-update.component';
import { DeviceTypeRoutingResolveService } from './device-type-routing-resolve.service';

const deviceTypeRoute: Routes = [
  {
    path: '',
    component: DeviceTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeviceTypeDetailComponent,
    resolve: {
      deviceType: DeviceTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeviceTypeUpdateComponent,
    resolve: {
      deviceType: DeviceTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeviceTypeUpdateComponent,
    resolve: {
      deviceType: DeviceTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deviceTypeRoute)],
  exports: [RouterModule],
})
export class DeviceTypeRoutingModule {}
