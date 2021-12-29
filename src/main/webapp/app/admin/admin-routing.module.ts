import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AdminLayoutComponent } from '../layouts/admin-layout/admin-layout.component';
/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

@NgModule({
  imports: [
    /* jhipster-needle-add-admin-module - JHipster will add admin modules here */
    RouterModule.forChild([
      {
        path: '',
        component: AdminLayoutComponent,
        children: [
          {
            path: 'user-management',
            loadChildren: () => import('./user-management/user-management.module').then(m => m.UserManagementModule),
            data: {
              pageTitle: 'userManagement.home.title',
            },
          },
          {
            path: 'docs',
            loadChildren: () => import('./docs/docs.module').then(m => m.DocsModule),
          },
          {
            path: 'configuration',
            loadChildren: () => import('./configuration/configuration.module').then(m => m.ConfigurationModule),
          },
          {
            path: 'health',
            loadChildren: () => import('./health/health.module').then(m => m.HealthModule),
          },
          {
            path: 'logs',
            loadChildren: () => import('./logs/logs.module').then(m => m.LogsModule),
          },
          {
            path: 'metrics',
            loadChildren: () => import('./metrics/metrics.module').then(m => m.MetricsModule),
          },
          {
            path: 'entities',
            loadChildren: () => import('../entities/entity-routing.module').then(m => m.EntityRoutingModule),
          },
        ],
      },

      /* jhipster-needle-add-admin-route - JHipster will add admin routes here */
    ]),
  ],
})
export class AdminRoutingModule {}
