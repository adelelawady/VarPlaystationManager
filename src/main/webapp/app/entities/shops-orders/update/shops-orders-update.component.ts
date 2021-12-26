import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IShopsOrders, ShopsOrders } from '../shops-orders.model';
import { ShopsOrdersService } from '../service/shops-orders.service';

@Component({
  selector: 'jhi-shops-orders-update',
  templateUrl: './shops-orders-update.component.html',
})
export class ShopsOrdersUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    totalPrice: [],
  });

  constructor(protected shopsOrdersService: ShopsOrdersService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shopsOrders }) => {
      this.updateForm(shopsOrders);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shopsOrders = this.createFromForm();
    if (shopsOrders.id !== undefined) {
      this.subscribeToSaveResponse(this.shopsOrdersService.update(shopsOrders));
    } else {
      this.subscribeToSaveResponse(this.shopsOrdersService.create(shopsOrders));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShopsOrders>>): void {
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

  protected updateForm(shopsOrders: IShopsOrders): void {
    this.editForm.patchValue({
      id: shopsOrders.id,
      name: shopsOrders.name,
      totalPrice: shopsOrders.totalPrice,
    });
  }

  protected createFromForm(): IShopsOrders {
    return {
      ...new ShopsOrders(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
    };
  }
}
