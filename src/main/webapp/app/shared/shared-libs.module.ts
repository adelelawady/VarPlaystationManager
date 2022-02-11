import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';
import { DxLoadIndicatorModule, DxSelectBoxModule, DxTextAreaModule, DxDateBoxModule, DxFormModule } from 'devextreme-angular';
@NgModule({
  exports: [
    FormsModule,
    CommonModule,
    RouterModule,
    NgbModule,
    InfiniteScrollModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    TranslateModule,
    DxDateBoxModule,
    DxLoadIndicatorModule,
    DxSelectBoxModule,
    DxTextAreaModule,
    DxFormModule,
  ],
})
export class SharedLibsModule {}
