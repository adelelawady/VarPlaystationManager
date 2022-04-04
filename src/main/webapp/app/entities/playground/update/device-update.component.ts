import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDevice, Device } from '../device.model';
import { DeviceTypeService } from '../../../../../../../bin/src/main/webapp/app/entities/device-type/service/device-type.service';
import { IDeviceType } from '../../../../../../../bin/src/main/webapp/app/entities/device-type/device-type.model';
import { DevicePlaygroundService } from 'app/entities/playground/service/device.service';
import * as dayjs from 'dayjs';
declare const $: any;
@Component({
  selector: 'jhi-device-update',
  templateUrl: './device-update.component.html',
})
export class DeviceUpdateComponent implements OnInit {
  isSaving = false;
  device: any;
  showAddTime = false;
  typesCollection: any[] = [];
  from = dayjs().startOf('day'); // set to 12:00 am today
  to = dayjs().endOf('day'); // set to 23:59 pm today
  editForm = this.fb.group({
    id: [],
    name: [],
    price: [],
  });

  constructor(
    protected deviceService: DevicePlaygroundService,
    protected deviceTypeService: DeviceTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.updateForm(device);
      this.device = device;
    });
  }

  fromDateChanged(date: any): void {
    // eslint-disable-next-line no-console
    this.from = dayjs(date.value);
    //this.loadPage();
  }
  toDateChanged(date: any): void {
    // eslint-disable-next-line no-console
    this.to = dayjs(date.value);
    //this.loadPage();
  }

  previousState(): void {
    window.history.back();
  }

  addNewTime(): void {
    const device = this.createFromForm();
    const price = $('#P_price').val();

    const req = {
      price,
      to: this.to,
      from: this.from,
    };
    // eslint-disable-next-line no-console
    console.log(price);
    console.log(this.from);
    console.log(this.to);
    if (device.id !== undefined) {
      this.deviceService.addTime(device.id, req).subscribe(() => {
        location.reload();
      });
    }
  }

  deleteTime(time: any): void {
    const device = this.createFromForm();
    if (device.id !== undefined) {
      this.deviceService.removeTime(device.id, time).subscribe(() => {
        location.reload();
      });
    }
  }
  save(): void {
    this.isSaving = true;
    const device = this.createFromForm();
    if (device.id !== undefined) {
      this.subscribeToSaveResponse(this.deviceService.update(device));
    } else {
      this.subscribeToSaveResponse(this.deviceService.create(device));
    }
  }

  trackDeviceTypeById(index: number, item: IDeviceType): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(device: any): void {
    this.editForm.patchValue({
      id: device.id,
      name: device.name,
      price: device.price,
    });
  }

  protected createFromForm(): any {
    return {
      ...new Device(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      price: this.editForm.get(['price'])!.value,
    };
  }
}
