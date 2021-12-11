import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SessionComponent } from './list/session.component';
import { SessionDetailComponent } from './detail/session-detail.component';
import { SessionUpdateComponent } from './update/session-update.component';
import { SessionDeleteDialogComponent } from './delete/session-delete-dialog.component';
import { SessionRoutingModule } from './route/session-routing.module';

@NgModule({
  imports: [SharedModule, SessionRoutingModule],
  declarations: [SessionComponent, SessionDetailComponent, SessionUpdateComponent, SessionDeleteDialogComponent],
  entryComponents: [SessionDeleteDialogComponent],
})
export class SessionModule {}
