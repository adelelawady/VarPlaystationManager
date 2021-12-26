import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ShopsOrdersComponent } from './list/shops-orders.component';
import { ShopsOrdersDetailComponent } from './detail/shops-orders-detail.component';
import { ShopsOrdersUpdateComponent } from './update/shops-orders-update.component';
import { ShopsOrdersDeleteDialogComponent } from './delete/shops-orders-delete-dialog.component';
import { ShopsOrdersRoutingModule } from './route/shops-orders-routing.module';

@NgModule({
  imports: [SharedModule, ShopsOrdersRoutingModule],
  declarations: [ShopsOrdersComponent, ShopsOrdersDetailComponent, ShopsOrdersUpdateComponent, ShopsOrdersDeleteDialogComponent],
  entryComponents: [ShopsOrdersDeleteDialogComponent],
})
export class ShopsOrdersModule {}
