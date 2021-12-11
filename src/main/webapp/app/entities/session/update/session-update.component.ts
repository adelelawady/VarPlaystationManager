import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISession, Session } from '../session.model';
import { SessionService } from '../service/session.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-session-update',
  templateUrl: './session-update.component.html',
})
export class SessionUpdateComponent implements OnInit {
  isSaving = false;

  devicesCollection: IDevice[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    start: [],
    reserved: [],
    device: [null, Validators.required],
    orders: [],
  });

  constructor(
    protected sessionService: SessionService,
    protected deviceService: DeviceService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ session }) => {
      if (session.id === undefined) {
        const today = dayjs().startOf('day');
        session.start = today;
      }

      this.updateForm(session);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const session = this.createFromForm();
    if (session.id !== undefined) {
      this.subscribeToSaveResponse(this.sessionService.update(session));
    } else {
      this.subscribeToSaveResponse(this.sessionService.create(session));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISession>>): void {
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

  protected updateForm(session: ISession): void {
    this.editForm.patchValue({
      id: session.id,
      start: session.start ? session.start.format(DATE_TIME_FORMAT) : null,
      reserved: session.reserved,
      device: session.device,
      orders: session.orders,
    });

    this.devicesCollection = this.deviceService.addDeviceToCollectionIfMissing(this.devicesCollection, session.device);
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      ...(session.orders ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query({ filter: 'session-is-null' })
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

  protected createFromForm(): ISession {
    return {
      ...new Session(),
      id: this.editForm.get(['id'])!.value,
      start: this.editForm.get(['start'])!.value ? dayjs(this.editForm.get(['start'])!.value, DATE_TIME_FORMAT) : undefined,
      reserved: this.editForm.get(['reserved'])!.value,
      device: this.editForm.get(['device'])!.value,
      orders: this.editForm.get(['orders'])!.value,
    };
  }
}
