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
import { SidebarAccordionModule } from 'ng-sidebar-accordion';
import { TableComponentComponent } from './table-component/table-component.component';
import { TablesTapComponentComponent } from '../pages/components/tables-tap-component/tables-tap-component.component';
import { CheckoutComponentComponent } from './checkout-component/checkout-component.component';
import { LocalizedDatePipe } from './utils/localized-date.pipe';
import { PosComponentComponent } from './pos-component/pos-component.component';
import { PlaygroundComponentComponent } from './playground-component/device-component.component';
import { DevicePlaygroundPricePipe } from './playground-component/price.pipe';

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
    DevicePlaygroundPricePipe,
    TableComponentComponent,
    TablesTapComponentComponent,
    CheckoutComponentComponent,
    LocalizedDatePipe,
    PosComponentComponent,
    PlaygroundComponentComponent,
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
    DevicePlaygroundPricePipe,
    SidebarAccordionModule,
    TableComponentComponent,
    TablesTapComponentComponent,
    CheckoutComponentComponent,
    LocalizedDatePipe,
    PosComponentComponent,
    PlaygroundComponentComponent,
  ],
  entryComponents: [],
  providers: [DevicePricePipe, LocalizedDatePipe, DevicePlaygroundPricePipe],
})
export class SharedModule {}
