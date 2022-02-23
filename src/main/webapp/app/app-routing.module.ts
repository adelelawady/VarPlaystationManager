import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
        },
        {
          path: 'sheft/current/report',
          data: {
            authorities: [Authority.USER],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./pages/single-sheft-report/single-sheft-report.module').then(m => m.SingleSheftReportModule),
        },
        {
          path: 'last/records',
          data: {
            authorities: [Authority.USER],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./pages/last-records-page/last-records-page.module').then(m => m.LastRecordsPageModule),
        },
        {
          path: 'last/records/sales',
          data: {
            authorities: [Authority.SALES],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./pages/last-records-page-sales/last-records-page.module').then(m => m.LastRecordsPageModule),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
        },
        {
          path: 'login',
          loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
        },
        ...LAYOUT_ROUTES,
      ]
      // { enableTracing: DEBUG_INFO_ENABLED }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
