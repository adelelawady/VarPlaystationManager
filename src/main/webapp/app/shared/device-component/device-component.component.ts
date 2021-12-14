import { Component, ChangeDetectionStrategy, Input, OnInit, ChangeDetectorRef, EventEmitter, Output } from '@angular/core';
import { DevicesSessionsService } from 'app/home/devicesSessions.service';

declare const $: any;

@Component({
  selector: 'jhi-device-component',
  templateUrl: './device-component.component.html',
  styleUrls: ['./device-component.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DeviceComponentComponent implements OnInit {
  isMulti = false;
  totalPriceUser = 0;
  @Input() device: any;
  @Output() deviceSelected = new EventEmitter();
  constructor(private cd: ChangeDetectorRef, private devicesSessionsService: DevicesSessionsService) {}
  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    null;
  }

  selectDevice(device: any): void {
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

  endSession(): void {
    this.isMulti = false;
    const sessionEnd = {
      totalPrice: this.totalPriceUser,
    };
    this.devicesSessionsService.stopDeviceSession(this.device.id, sessionEnd).subscribe(dev => {
      this.device = dev;
    });
  }
}
