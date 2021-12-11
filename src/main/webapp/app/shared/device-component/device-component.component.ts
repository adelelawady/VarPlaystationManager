import { Component, ChangeDetectionStrategy, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { DevicesSessionsService } from 'app/home/devicesSessions.service';

@Component({
  selector: 'jhi-device-component',
  templateUrl: './device-component.component.html',
  styleUrls: ['./device-component.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DeviceComponentComponent implements OnInit {
  @Input() device: any;
  constructor(private cd: ChangeDetectorRef, private devicesSessionsService: DevicesSessionsService) {}
  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    null;
  }

  startSession(): void {
    this.devicesSessionsService.startDeviceSession(this.device.id).subscribe(dev => {
      this.device = dev;

      this.cd.markForCheck();
    });
  }

  endSession(): void {
    this.devicesSessionsService.stopDeviceSession(this.device.id).subscribe(dev => {
      this.device = dev;
    });
  }
}
