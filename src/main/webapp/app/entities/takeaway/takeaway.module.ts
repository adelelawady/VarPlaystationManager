import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TakeawayComponent } from './list/takeaway.component';
import { TakeawayDetailComponent } from './detail/takeaway-detail.component';
import { TakeawayUpdateComponent } from './update/takeaway-update.component';
import { TakeawayDeleteDialogComponent } from './delete/takeaway-delete-dialog.component';
import { TakeawayRoutingModule } from './route/takeaway-routing.module';

@NgModule({
  imports: [SharedModule, TakeawayRoutingModule],
  declarations: [TakeawayComponent, TakeawayDetailComponent, TakeawayUpdateComponent, TakeawayDeleteDialogComponent],
  entryComponents: [TakeawayDeleteDialogComponent],
})
export class TakeawayModule {}
