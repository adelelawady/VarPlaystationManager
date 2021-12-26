import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITable } from '../table.model';
import { TableService } from '../service/table.service';

@Component({
  templateUrl: './table-delete-dialog.component.html',
})
export class TableDeleteDialogComponent {
  table?: ITable;

  constructor(protected tableService: TableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.tableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
