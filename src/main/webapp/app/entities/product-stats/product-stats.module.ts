import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TableComponent } from './list/table.component';
import { TableDetailComponent } from './detail/table-detail.component';
import { TableRoutingModule } from './route/table-routing.module';

@NgModule({
  imports: [SharedModule, TableRoutingModule],
  declarations: [TableComponent, TableDetailComponent],
  entryComponents: [],
})
export class ProductStatsModule {}
