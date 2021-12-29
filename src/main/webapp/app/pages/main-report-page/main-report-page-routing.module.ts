import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MainReportPageComponent } from './main-report-page.component';

const takeawayRoute: Routes = [
  {
    path: '',
    component: MainReportPageComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(takeawayRoute)],
  exports: [RouterModule],
})
export class MainReportPageRoutingModule {}
