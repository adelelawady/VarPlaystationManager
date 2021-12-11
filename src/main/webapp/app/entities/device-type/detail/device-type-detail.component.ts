import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeviceType } from '../device-type.model';

@Component({
  selector: 'jhi-device-type-detail',
  templateUrl: './device-type-detail.component.html',
})
export class DeviceTypeDetailComponent implements OnInit {
  deviceType: IDeviceType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceType }) => {
      this.deviceType = deviceType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
