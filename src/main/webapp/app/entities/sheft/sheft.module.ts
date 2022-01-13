import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SheftComponent } from './list/sheft.component';
import { SheftDetailComponent } from './detail/sheft-detail.component';
import { SheftDeleteDialogComponent } from './delete/sheft-delete-dialog.component';
import { SheftRoutingModule } from './route/sheft-routing.module';

@NgModule({
  imports: [SharedModule, SheftRoutingModule],
  declarations: [SheftComponent, SheftDetailComponent, SheftDeleteDialogComponent],
  entryComponents: [SheftDeleteDialogComponent],
})
export class SheftModule {}
