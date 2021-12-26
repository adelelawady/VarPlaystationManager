import { Component, ChangeDetectionStrategy, Input, OnInit, ChangeDetectorRef, EventEmitter, Output } from '@angular/core';
import { DevicesSessionsService } from 'app/home/devicesSessions.service';
import { async } from 'rxjs';
import { DevicePricePipe } from './price.pipe';

declare const $: any;

@Component({
  selector: 'jhi-device-component',
  templateUrl: './device-component.component.html',
  styleUrls: ['./device-component.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DeviceComponentComponent implements OnInit {
  isMulti = false;
  print = true;
  discount = 0.0;
  totalPriceUser = 0;
  @Input() isSelected = false;
  @Input() device: any;
  @Output() deviceSelected = new EventEmitter();
  @Output() deviceClicked = new EventEmitter();
  constructor(
    private devicePricePipe: DevicePricePipe,
    private cd: ChangeDetectorRef,
    private devicesSessionsService: DevicesSessionsService
  ) {}
  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    this.getDevicePrice();
  }
  OnDeviceClicked(): void {
    this.totalPriceUser = this.getDevicePrice();
    this.deviceClicked.emit(this.device);
  }

  selectDevice(device: any): void {
    // eslint-disable-next-line no-console
    this.deviceSelected.emit(device);
  }
  startSession(): void {
    const sessionStart = {
      multi: this.isMulti,
      reserved: 0,
    };

    this.devicesSessionsService.startDeviceSession(this.device.id, sessionStart).subscribe(dev => {
      this.device = dev;

      this.cd.markForCheck();
    });
  }

  getDevicePrice(): any {
    let diff = (new Date(this.device.session.start).getTime() - new Date().getTime()) / 1000;
    diff /= 60;
    const diffMin = Math.abs(Math.round(diff));
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands

    let pricePerHour = this.device.session.device.type.pricePerHour;
    if (this.device.session.multi) {
      pricePerHour = this.device.session.device.type.pricePerHourMulti;
    } else {
      pricePerHour = this.device.session.device.type.pricePerHour;
    }

    const totalOrderprice = this.device.session.ordersPrice ? this.device.session.ordersPrice : 0;

    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    const res = Math.round((diffMin / 60) * pricePerHour) + totalOrderprice;
    if (res > 0) {
      if (this.discount && this.discount <= 100 && this.discount > 0) {
        const resdis = Math.round(((100 - this.discount) * res) / 100);
        this.totalPriceUser = resdis;
        this.cd.markForCheck();
        return resdis;
      } else {
        this.totalPriceUser = res;
        this.cd.markForCheck();
        return res;
      }
    } else {
      return 0;
    }
  }
  formateMoney(r: any): any {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return 'EGP ' + r.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
  }
  endSession(): void {
    this.isMulti = false;
    const sessionEnd = {
      totalPrice: this.totalPriceUser,
      print: this.print,
      discount: this.discount,
    };
    this.devicesSessionsService.stopDeviceSession(this.device.id, sessionEnd).subscribe(dev => {
      this.device = dev;
      this.cd.markForCheck();
    });
  }
}
