import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISheft } from '../sheft.model';
import { SheftService } from '../service/sheft.service';

@Component({
  templateUrl: './sheft-delete-dialog.component.html',
})
export class SheftDeleteDialogComponent {
  sheft?: ISheft;

  constructor(protected sheftService: SheftService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.sheftService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
