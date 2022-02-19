import { AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DevicesSessionsService } from 'app/home/devicesSessions.service';
import { DevicePricePipe } from '../device-component/price.pipe';
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

  isSaving = false;
  isMulti = false;
  plusMinutes = 0;
  @Input() device: any;

  @Output() closeCheckOut = new EventEmitter();
  @Output() deviceStopped = new EventEmitter();
  @Output() deviceStarted = new EventEmitter();

  constructor(
    private devicePricePipe: DevicePricePipe,
    private cd: ChangeDetectorRef,
    private devicesSessionsService: DevicesSessionsService
  ) {}
  ngAfterViewInit(): void {
    this.getDevicePrice();
  }
  ngOnInit(): void {
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
  }
}
