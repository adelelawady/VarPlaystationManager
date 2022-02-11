import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { DxButtonModule } from 'devextreme-angular';
import { DxTextBoxModule, DxTextAreaModule } from 'devextreme-angular';
import { DxDateBoxModule } from 'devextreme-angular';
import { DxButtonGroupModule } from 'devextreme-angular';

@NgModule({
  imports: [SharedModule, DxButtonGroupModule, DxDateBoxModule, DxTextBoxModule, DxTextAreaModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class HomeModule {}
