import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecord } from '../record.model';
import { RecordService } from '../service/record.service';

@Component({
  templateUrl: './record-delete-dialog.component.html',
})
export class RecordDeleteDialogComponent {
  record?: IRecord;

  constructor(protected recordService: RecordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.recordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
