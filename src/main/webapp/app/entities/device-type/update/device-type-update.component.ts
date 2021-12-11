import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDeviceType, DeviceType } from '../device-type.model';
import { DeviceTypeService } from '../service/device-type.service';

@Component({
  selector: 'jhi-device-type-update',
  templateUrl: './device-type-update.component.html',
})
export class DeviceTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    pricePerHour: [],
  });

  constructor(protected deviceTypeService: DeviceTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceType }) => {
      this.updateForm(deviceType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceType = this.createFromForm();
    if (deviceType.id !== undefined) {
      this.subscribeToSaveResponse(this.deviceTypeService.update(deviceType));
    } else {
      this.subscribeToSaveResponse(this.deviceTypeService.create(deviceType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceType>>): void {
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

  protected updateForm(deviceType: IDeviceType): void {
    this.editForm.patchValue({
      id: deviceType.id,
      name: deviceType.name,
      pricePerHour: deviceType.pricePerHour,
    });
  }

  protected createFromForm(): IDeviceType {
    return {
      ...new DeviceType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      pricePerHour: this.editForm.get(['pricePerHour'])!.value,
    };
  }
}
