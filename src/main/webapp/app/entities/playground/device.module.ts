import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeviceComponent } from './list/device.component';
import { DeviceDetailComponent } from './detail/device-detail.component';
import { DeviceDeleteDialogComponent } from './delete/device-delete-dialog.component';
import { DeviceRoutingModule } from './route/device-routing.module';
import { DeviceUpdateComponent } from './update/device-update.component';

@NgModule({
  imports: [SharedModule, DeviceRoutingModule],
  declarations: [DeviceComponent, DeviceDetailComponent, DeviceUpdateComponent, DeviceDeleteDialogComponent],
  entryComponents: [DeviceDeleteDialogComponent],
})
export class DeviceModule {}
