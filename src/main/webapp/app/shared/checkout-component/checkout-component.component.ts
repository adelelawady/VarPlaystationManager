import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { DevicesSessionsService } from 'app/home/devicesSessions.service';
import { DevicePricePipe } from '../device-component/price.pipe';
import { Authority } from '../../config/authority.constants';
import { PlaygroundDevicesSessionsService } from '../../home/playgrounddevicesSessions.service';
declare const $: any;
@Component({
  selector: 'jhi-checkout-component',
  templateUrl: './checkout-component.component.html',
  styleUrls: ['./checkout-component.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CheckoutComponentComponent implements OnInit, AfterViewInit {
  print = true;
  timeDiscount = 0.0;
  ordersDiscount = 0.0;
  totalPriceUser: any;
  totalDiscountPrice = 0.0;
  account: Account | null = null;
  isSaving = false;
  isMulti = false;
  plusMinutes = 0;
  isSales = false;
  @Input() device: any;
  @Input() selectedType = 'device';

  @Output() closeCheckOut = new EventEmitter();
  @Output() deviceStopped = new EventEmitter();
  @Output() deviceStarted = new EventEmitter();

  constructor(
    private devicePricePipe: DevicePricePipe,
    private cd: ChangeDetectorRef,
    private devicesSessionsService: DevicesSessionsService,
    private playgroundDevicesSessionsService: PlaygroundDevicesSessionsService,
    private accountService: AccountService
  ) {}
  ngAfterViewInit(): void {
    this.getDevicePrice();
  }
  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account?.authorities.includes(Authority.SALES)) {
        this.isSales = true;
      }
      this.account = account;
    });

    this.getDevicePrice();
  }

  getDevicePrice(): void {
    if (this.device?.session) {
      this.totalPriceUser = this.devicePricePipe.transform(this.device, true, this.timeDiscount, this.ordersDiscount, false, false);
      this.discountPrice();
      this.cd.markForCheck();
    }
  }
  discountPrice(): void {
    const totalPriceBeforeDiscount = this.devicePricePipe.transform(
      this.device,
      true,
      this.timeDiscount,
      this.ordersDiscount,
      false,
      false,
      false
    );

    const totalPriceAfterDiscount = this.devicePricePipe.transform(this.device, true, 0, 0, false, false, false);

    this.totalDiscountPrice = totalPriceAfterDiscount - totalPriceBeforeDiscount;
    this.cd.markForCheck();
  }
  count(n: any, r: any): any {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return n + r;
  }
  cancel(): void {
    this.closeCheckOut.emit();
  }
  startSession(): void {
    this.isSaving = true;
    const sessionStart = {
      multi: this.isMulti,
      reserved: 0,
      plusMinutes: this.plusMinutes,
    };

    if (this.selectedType === 'device') {
      this.devicesSessionsService.startDeviceSession(this.device.id, sessionStart).subscribe(
        dev => {
          this.device = dev;

          this.cd.markForCheck();
          this.deviceStarted.emit(this.device);
          this.isSaving = false;
        },
        () => {
          this.isSaving = false;
        }
      );
    } else {
      this.playgroundDevicesSessionsService.startDeviceSession(this.device.id, sessionStart).subscribe(
        dev => {
          this.device = dev;

          this.cd.markForCheck();
          this.deviceStarted.emit(this.device);
          this.isSaving = false;
        },
        () => {
          this.isSaving = false;
        }
      );
    }
  }

  endSession(): void {
    this.isMulti = false;
    this.isSaving = true;
    const sessionEnd = {
      totalPrice: this.totalPriceUser,
      print: this.print,
      ordersDiscount: this.ordersDiscount,
      timeDiscount: this.timeDiscount,
    };
    if (this.selectedType === 'device') {
      this.devicesSessionsService.stopDeviceSession(this.device.id, sessionEnd).subscribe(
        dev => {
          this.device = dev;
          this.cd.markForCheck();
          this.deviceStopped.emit(this);
          this.isSaving = false;
        },
        () => {
          this.isSaving = false;
        }
      );
    } else {
      this.playgroundDevicesSessionsService.stopDeviceSession(this.device.id, sessionEnd).subscribe(
        dev => {
          this.device = dev;
          this.cd.markForCheck();
          this.deviceStopped.emit(this);
          this.isSaving = false;
        },
        () => {
          this.isSaving = false;
        }
      );
    }
  }
}
