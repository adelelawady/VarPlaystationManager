import { MainReportPageRoutingModule } from './main-report-page-routing.module';
import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MainReportPageComponent } from './main-report-page.component';
import { DatePipe } from '@angular/common';

@NgModule({
  imports: [SharedModule, MainReportPageRoutingModule],
  declarations: [MainReportPageComponent],
  entryComponents: [],
  providers: [DatePipe],
})
export class MainReportPageModule {}
