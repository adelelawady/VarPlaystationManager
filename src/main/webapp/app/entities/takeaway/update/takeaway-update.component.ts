import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITakeaway, Takeaway } from '../takeaway.model';
import { TakeawayService } from '../service/takeaway.service';

@Component({
  selector: 'jhi-takeaway-update',
  templateUrl: './takeaway-update.component.html',
})
export class TakeawayUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    totalPrice: [],
  });

  constructor(protected takeawayService: TakeawayService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ takeaway }) => {
      this.updateForm(takeaway);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const takeaway = this.createFromForm();
    if (takeaway.id !== undefined) {
      this.subscribeToSaveResponse(this.takeawayService.update(takeaway));
    } else {
      this.subscribeToSaveResponse(this.takeawayService.create(takeaway));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITakeaway>>): void {
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

  protected updateForm(takeaway: ITakeaway): void {
    this.editForm.patchValue({
      id: takeaway.id,
      totalPrice: takeaway.totalPrice,
    });
  }

  protected createFromForm(): ITakeaway {
    return {
      ...new Takeaway(),
      id: this.editForm.get(['id'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
    };
  }
}
