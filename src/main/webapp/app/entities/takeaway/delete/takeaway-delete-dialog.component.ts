import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITakeaway } from '../takeaway.model';
import { TakeawayService } from '../service/takeaway.service';

@Component({
  templateUrl: './takeaway-delete-dialog.component.html',
})
export class TakeawayDeleteDialogComponent {
  takeaway?: ITakeaway;

  constructor(protected takeawayService: TakeawayService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.takeawayService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
