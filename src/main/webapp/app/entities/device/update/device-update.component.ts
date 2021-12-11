import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDevice, Device } from '../device.model';
import { DeviceService } from '../service/device.service';
import { IDeviceType } from 'app/entities/device-type/device-type.model';
import { DeviceTypeService } from 'app/entities/device-type/service/device-type.service';

@Component({
  selector: 'jhi-device-update',
  templateUrl: './device-update.component.html',
})
export class DeviceUpdateComponent implements OnInit {
  isSaving = false;

  typesCollection: IDeviceType[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    type: [null, Validators.required],
  });

  constructor(
    protected deviceService: DeviceService,
    protected deviceTypeService: DeviceTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.updateForm(device);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
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

  protected updateForm(device: IDevice): void {
    this.editForm.patchValue({
      id: device.id,
      name: device.name,
      type: device.type,
    });

    this.typesCollection = this.deviceTypeService.addDeviceTypeToCollectionIfMissing(this.typesCollection, device.type);
  }

  protected loadRelationshipsOptions(): void {
    this.deviceTypeService
      .query({ filter: 'device-is-null' })
      .pipe(map((res: HttpResponse<IDeviceType[]>) => res.body ?? []))
      .pipe(
        map((deviceTypes: IDeviceType[]) =>
          this.deviceTypeService.addDeviceTypeToCollectionIfMissing(deviceTypes, this.editForm.get('type')!.value)
        )
      )
      .subscribe((deviceTypes: IDeviceType[]) => (this.typesCollection = deviceTypes));
  }

  protected createFromForm(): IDevice {
    return {
      ...new Device(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
