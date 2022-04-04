import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITableRecord } from '../table-record.model';
import { TableRecordService } from '../service/table-record.service';

@Component({
  templateUrl: './table-record-delete-dialog.component.html',
})
export class TableRecordDeleteDialogComponent {
  tableRecord?: ITableRecord;

  constructor(protected tableRecordService: TableRecordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.tableRecordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
