import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeviceComponent } from '../list/device.component';
import { DeviceDetailComponent } from '../detail/device-detail.component';
import { DeviceUpdateComponent } from '../update/device-update.component';
import { DeviceRoutingResolveService } from './device-routing-resolve.service';

const deviceRoute: Routes = [
  {
    path: '',
    component: DeviceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeviceDetailComponent,
    resolve: {
      device: DeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeviceUpdateComponent,
    resolve: {
      device: DeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeviceUpdateComponent,
    resolve: {
      device: DeviceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deviceRoute)],
  exports: [RouterModule],
})
export class DeviceRoutingModule {}
