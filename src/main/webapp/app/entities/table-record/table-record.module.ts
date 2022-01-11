import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TableRecordComponent } from './list/table-record.component';
import { TableRecordDetailComponent } from './detail/table-record-detail.component';
import { TableRecordDeleteDialogComponent } from './delete/table-record-delete-dialog.component';
import { TableRecordRoutingModule } from './route/table-record-routing.module';
import { TableRecordTakeawayComponent } from './list-takeaway/table-record.component';
import { TableRecordShopsComponent } from './list-shops/table-record.component';

@NgModule({
  imports: [SharedModule, TableRecordRoutingModule],
  declarations: [
    TableRecordComponent,
    TableRecordDetailComponent,
    TableRecordDeleteDialogComponent,
    TableRecordShopsComponent,
    TableRecordTakeawayComponent,
  ],
  entryComponents: [TableRecordDeleteDialogComponent],
})
export class TableRecordModule {}
