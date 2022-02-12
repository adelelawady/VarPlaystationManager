import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LastRecordsPageComponent } from './last-records-page.component';

const takeawayRoute: Routes = [
  {
    path: '',
    component: LastRecordsPageComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(takeawayRoute)],
  exports: [RouterModule],
})
export class LastRecordsPageRoutingModule {}
