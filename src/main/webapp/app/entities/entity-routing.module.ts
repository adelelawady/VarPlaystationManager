import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MainReportPageComponent } from '../pages/main-report-page/main-report-page.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'device-type',
        data: { pageTitle: 'erApp.deviceType.home.title' },
        loadChildren: () => import('./device-type/device-type.module').then(m => m.DeviceTypeModule),
      },
      {
        path: 'device',
        data: { pageTitle: 'erApp.device.home.title' },
        loadChildren: () => import('./device/device.module').then(m => m.DeviceModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'erApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'erApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'record',
        data: { pageTitle: 'erApp.record.home.title' },
        loadChildren: () => import('./record/record.module').then(m => m.RecordModule),
      },
      {
        path: 'session',
        data: { pageTitle: 'erApp.session.home.title' },
        loadChildren: () => import('./session/session.module').then(m => m.SessionModule),
      },
      {
        path: 'shops-orders',
        data: { pageTitle: 'erApp.shopsOrders.home.title' },
        loadChildren: () => import('./shops-orders/shops-orders.module').then(m => m.ShopsOrdersModule),
      },
      {
        path: 'table',
        data: { pageTitle: 'erApp.table.home.title' },
        loadChildren: () => import('./table/table.module').then(m => m.TableModule),
      },
      {
        path: 'takeaway',
        data: { pageTitle: 'erApp.takeaway.home.title' },
        loadChildren: () => import('./takeaway/takeaway.module').then(m => m.TakeawayModule),
      },

      {
        path: 'main-report',
        data: { pageTitle: 'erApp.takeaway.home.title' },
        loadChildren: () => import('../pages/main-report-page/main-report-page.module').then(m => m.MainReportPageModule),
      },
      {
        path: 'table-record',
        data: { pageTitle: 'erApp.tableRecord.home.title' },
        loadChildren: () => import('./table-record/table-record.module').then(m => m.TableRecordModule),
      },
      {
        path: 'table-record',
        data: { pageTitle: 'erApp.tableRecord.home.title' },
        loadChildren: () => import('./table-record/table-record.module').then(m => m.TableRecordModule),
      },
      {
        path: 'table-record',
        data: { pageTitle: 'erApp.tableRecord.home.title' },
        loadChildren: () => import('./table-record/table-record.module').then(m => m.TableRecordModule),
      },
      {
        path: 'shefts',
        data: { pageTitle: 'erApp.tableRecord.home.title' },
        loadChildren: () => import('./sheft/sheft.module').then(m => m.SheftModule),
      },
      {
        path: 'product-stats',
        data: { pageTitle: 'erApp.tableRecord.home.title' },
        loadChildren: () => import('./product-stats/product-stats.module').then(m => m.ProductStatsModule),
      },

      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
