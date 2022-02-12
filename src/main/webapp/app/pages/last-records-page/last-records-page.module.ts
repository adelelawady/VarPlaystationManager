import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DatePipe } from '@angular/common';
import { LastRecordsPageRoutingModule } from './last-records-page-routing.module';
import { LastRecordsPageComponent } from './last-records-page.component';

@NgModule({
  imports: [SharedModule, LastRecordsPageRoutingModule],
  declarations: [LastRecordsPageComponent],
  entryComponents: [],
  providers: [DatePipe],
})
export class LastRecordsPageModule {}
