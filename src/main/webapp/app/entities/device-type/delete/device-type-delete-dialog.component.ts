import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeviceType } from '../device-type.model';
import { DeviceTypeService } from '../service/device-type.service';

@Component({
  templateUrl: './device-type-delete-dialog.component.html',
})
export class DeviceTypeDeleteDialogComponent {
  deviceType?: IDeviceType;

  constructor(protected deviceTypeService: DeviceTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.deviceTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
