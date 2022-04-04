import { Component, ChangeDetectionStrategy, Input, OnInit, ChangeDetectorRef, EventEmitter, Output, AfterViewInit } from '@angular/core';
import { DevicesSessionsService } from 'app/home/devicesSessions.service';
import { DevicePlaygroundPricePipe } from './price.pipe';

declare const $: any;

@Component({
  selector: 'jhi-playground-component',
  templateUrl: './device-component.component.html',
  styleUrls: ['./device-component.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlaygroundComponentComponent implements OnInit, AfterViewInit {
  isMulti = false;
  print = true;
  timeDiscount = 0.0;
  ordersDiscount = 0.0;
  totalPriceUser: any;
  plusMinutes = 0;
  showOptions = false;
  devices: any[] | undefined;
  showPreviousSessions = false;
  showCloseButton = false;
  showOptionsTxt = false;
  showPreviousSessionsTxt = false;
  showCloseSessionsTxt = false;

  isSingleClick = true;
  disableClick = false;
  @Input() isSelected = false;
  @Input() device: any;
  @Output() deviceSelected = new EventEmitter();
  @Output() deviceClicked = new EventEmitter();
  @Output() deviceStopped = new EventEmitter();
  @Output() deviceMoved = new EventEmitter();
  @Output() deviceDoubleClicked = new EventEmitter();
  @Output() deviceCheckOut = new EventEmitter();

  constructor(
    private devicePricePipe: DevicePlaygroundPricePipe,
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
  loadAllDevices(): void {
    this.devicesSessionsService.getDevicesSessions().subscribe((devicesFound: any) => {
      this.devices = devicesFound.filter((device: any) => !device.session);
    });
  }

  singleClick(): void {
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const self = this;
    if (this.disableClick) {
      // eslint-disable-next-line @typescript-eslint/no-this-alias

      setTimeout(() => {
        self.disableClick = false;
      }, 1000);
      return;
    }

    self.isSingleClick = true;
    setTimeout(() => {
      if (self.isSingleClick) {
        self.deviceSelected.emit(self.device);
      }
    }, 250);
  }
  dblClick(): void {
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const self = this;
    if (this.disableClick) {
      // eslint-disable-next-line @typescript-eslint/no-this-alias

      setTimeout(() => {
        self.disableClick = false;
      }, 1000);
      return;
    }
    this.isSingleClick = false;
    if (!this.device.session) {
      this.openCheckOut();
    } else {
      this.getDevicePrice();
      this.deviceDoubleClicked.emit(this.device);
    }
  }
  diableClick(): void {
    this.disableClick = true;
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const self = this;
    setTimeout(() => {
      // eslint-disable-next-line @typescript-eslint/no-this-alias

      self.disableClick = false;
    }, 1000);
  }
  selectDevice(device: any): void {
    // eslint-disable-next-line no-console
    this.deviceSelected.emit(device);
  }
  startSession(): void {
    const sessionStart = {
      multi: this.isMulti,
      reserved: 0,
      plusMinutes: this.plusMinutes,
    };

    this.devicesSessionsService.startDeviceSession(this.device.id, sessionStart).subscribe(dev => {
      this.device = dev;

      this.cd.markForCheck();
    });
  }

  openCheckOut(): void {
    this.getDevicePrice();
    this.deviceCheckOut.emit(this);
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    //$('#modal' + this.device.id).modal();
    this.cd.markForCheck();
  }
  getDevicePrice(): void {
    if (this.device?.session) {
      this.totalPriceUser = this.devicePricePipe.transform(this.device, true, this.timeDiscount, this.ordersDiscount, false, false);
    }
  }
  moveToDevice(dev: any): void {
    this.devicesSessionsService.moveDevice(this.device.id, dev.id).subscribe((devicesFound: any) => {
      this.deviceMoved.emit(devicesFound);
      this.cd.markForCheck();
    });
  }

  moveToDeviceMulti(): void {
    this.devicesSessionsService.moveDeviceToMulti(this.device.id, !this.device.session.multi).subscribe((devicesFound: any) => {
      this.deviceMoved.emit(devicesFound);
      this.cd.markForCheck();
    });
  }

  formateMoney(r: any): any {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return (
      'EGP ' +
      Number(r)
        .toFixed(2)
        .replace(/\d(?=(\d{3})+\.)/g, '$&,')
    );
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
