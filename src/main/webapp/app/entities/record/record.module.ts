import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RecordComponent } from './list/record.component';
import { RecordDetailComponent } from './detail/record-detail.component';
import { RecordUpdateComponent } from './update/record-update.component';
import { RecordDeleteDialogComponent } from './delete/record-delete-dialog.component';
import { RecordRoutingModule } from './route/record-routing.module';

@NgModule({
  imports: [SharedModule, RecordRoutingModule],
  declarations: [RecordComponent, RecordDetailComponent, RecordUpdateComponent, RecordDeleteDialogComponent],
  entryComponents: [RecordDeleteDialogComponent],
})
export class RecordModule {}
