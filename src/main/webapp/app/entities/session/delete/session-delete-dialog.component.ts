import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISession } from '../session.model';
import { SessionService } from '../service/session.service';

@Component({
  templateUrl: './session-delete-dialog.component.html',
})
export class SessionDeleteDialogComponent {
  session?: ISession;

  constructor(protected sessionService: SessionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.sessionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
