import { Component, ChangeDetectionStrategy, Input, OnInit, ChangeDetectorRef, EventEmitter, Output, AfterViewInit } from '@angular/core';
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
export class DeviceComponentComponent implements OnInit, AfterViewInit {
  isMulti = false;
  print = true;
  timeDiscount = 0.0;
  ordersDiscount = 0.0;
  totalPriceUser: any;
  @Input() isSelected = false;
  @Input() device: any;
  @Output() deviceSelected = new EventEmitter();
  @Output() deviceClicked = new EventEmitter();
  @Output() deviceStopped = new EventEmitter();
  constructor(
    private devicePricePipe: DevicePricePipe,
    private cd: ChangeDetectorRef,
    private devicesSessionsService: DevicesSessionsService
  ) {}
  ngAfterViewInit(): void {
    this.getDevicePrice();
  }
  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    this.getDevicePrice();
  }

  OnDeviceClicked(): void {
    this.getDevicePrice();

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
  getDevicePrice(): void {
    if (this.device?.session) {
      this.totalPriceUser = this.devicePricePipe.transform(this.device, true, this.timeDiscount, this.ordersDiscount, false, false);
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
      ordersDiscount: this.ordersDiscount,
      timeDiscount: this.timeDiscount,
    };
    this.devicesSessionsService.stopDeviceSession(this.device.id, sessionEnd).subscribe(dev => {
      this.device = dev;
      this.cd.markForCheck();
      this.deviceStopped.emit(this);
    });
  }
}
