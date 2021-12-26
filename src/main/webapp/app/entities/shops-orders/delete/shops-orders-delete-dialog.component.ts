import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IShopsOrders } from '../shops-orders.model';
import { ShopsOrdersService } from '../service/shops-orders.service';

@Component({
  templateUrl: './shops-orders-delete-dialog.component.html',
})
export class ShopsOrdersDeleteDialogComponent {
  shopsOrders?: IShopsOrders;

  constructor(protected shopsOrdersService: ShopsOrdersService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.shopsOrdersService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
