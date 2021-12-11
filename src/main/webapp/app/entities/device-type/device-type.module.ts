import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeviceTypeComponent } from './list/device-type.component';
import { DeviceTypeDetailComponent } from './detail/device-type-detail.component';
import { DeviceTypeUpdateComponent } from './update/device-type-update.component';
import { DeviceTypeDeleteDialogComponent } from './delete/device-type-delete-dialog.component';
import { DeviceTypeRoutingModule } from './route/device-type-routing.module';

@NgModule({
  imports: [SharedModule, DeviceTypeRoutingModule],
  declarations: [DeviceTypeComponent, DeviceTypeDetailComponent, DeviceTypeUpdateComponent, DeviceTypeDeleteDialogComponent],
  entryComponents: [DeviceTypeDeleteDialogComponent],
})
export class DeviceTypeModule {}
