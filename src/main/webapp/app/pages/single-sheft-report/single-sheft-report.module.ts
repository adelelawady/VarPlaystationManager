import { SingleSheftReportRoutingModule } from './single-sheft-report-routing.module';
import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DatePipe } from '@angular/common';
import { SingleSheftReportComponent } from './single-sheft-report.component';

@NgModule({
  imports: [SharedModule, SingleSheftReportRoutingModule],
  declarations: [SingleSheftReportComponent],
  entryComponents: [],
  providers: [DatePipe],
})
export class SingleSheftReportModule {}
