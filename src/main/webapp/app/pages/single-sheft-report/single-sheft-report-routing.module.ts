import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SingleSheftReportComponent } from './single-sheft-report.component';

const takeawayRoute: Routes = [
  {
    path: '',
    component: SingleSheftReportComponent,
    // canActivate: [UserRouteAccessService],
    data: {
      pageTitle: 'global.title',
      // authorities: ['ROLE_ADMIN'],
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(takeawayRoute)],
  exports: [RouterModule],
})
export class SingleSheftReportRoutingModule {}
