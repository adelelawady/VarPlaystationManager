import { NgModule } from '@angular/core';

import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { DeviceComponentComponent } from './device-component/device-component.component';
import { CountdownPipe } from './device-component/countdown.pipe';
import { DevicePricePipe } from './device-component/price.pipe';
import { SidebarModule } from 'ng-sidebar';
import { SidebarAccordionModule } from 'ng-sidebar-accordion';
import { DevicePricePipeTimeOnly } from './device-component/priceTimeOnly.pipe';
import { TableComponentComponent } from './table-component/table-component.component';

@NgModule({
  imports: [SharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    DeviceComponentComponent,
    CountdownPipe,
    DevicePricePipe,
    DevicePricePipeTimeOnly,
    TableComponentComponent,
  ],
  exports: [
    SharedLibsModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    DeviceComponentComponent,
    CountdownPipe,
    DevicePricePipe,
    SidebarAccordionModule,
    DevicePricePipeTimeOnly,
    TableComponentComponent,
  ],
  providers: [DevicePricePipe],
})
export class SharedModule {}
