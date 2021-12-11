import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRecord, Record } from '../record.model';
import { RecordService } from '../service/record.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-record-update',
  templateUrl: './record-update.component.html',
})
export class RecordUpdateComponent implements OnInit {
  isSaving = false;

  devicesCollection: IDevice[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    start: [],
    end: [],
    totalPrice: [],
    device: [null, Validators.required],
    orders: [],
  });

  constructor(
    protected recordService: RecordService,
    protected deviceService: DeviceService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ record }) => {
      if (record.id === undefined) {
        const today = dayjs().startOf('day');
        record.start = today;
        record.end = today;
      }

      this.updateForm(record);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const record = this.createFromForm();
    if (record.id !== undefined) {
      this.subscribeToSaveResponse(this.recordService.update(record));
    } else {
      this.subscribeToSaveResponse(this.recordService.create(record));
    }
  }

  trackDeviceById(index: number, item: IDevice): string {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): string {
    return item.id!;
  }

  getSelectedProduct(option: IProduct, selectedVals?: IProduct[]): IProduct {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecord>>): void {
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

  protected updateForm(record: IRecord): void {
    this.editForm.patchValue({
      id: record.id,
      start: record.start ? record.start.format(DATE_TIME_FORMAT) : null,
      end: record.end ? record.end.format(DATE_TIME_FORMAT) : null,
      totalPrice: record.totalPrice,
      device: record.device,
      orders: record.orders,
    });

    this.devicesCollection = this.deviceService.addDeviceToCollectionIfMissing(this.devicesCollection, record.device);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      ...(record.orders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query({ filter: 'record-is-null' })
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing(devices, this.editForm.get('device')!.value)))
      .subscribe((devices: IDevice[]) => (this.devicesCollection = devices));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) =>
          this.productService.addProductToCollectionIfMissing(products, ...(this.editForm.get('orders')!.value ?? []))
        )
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): IRecord {
    return {
      ...new Record(),
      id: this.editForm.get(['id'])!.value,
      start: this.editForm.get(['start'])!.value ? dayjs(this.editForm.get(['start'])!.value, DATE_TIME_FORMAT) : undefined,
      end: this.editForm.get(['end'])!.value ? dayjs(this.editForm.get(['end'])!.value, DATE_TIME_FORMAT) : undefined,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
      device: this.editForm.get(['device'])!.value,
      orders: this.editForm.get(['orders'])!.value,
    };
  }
}
