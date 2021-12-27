import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITable, Table } from '../table.model';
import { TableService } from '../service/table.service';

@Component({
  selector: 'jhi-table-update',
  templateUrl: './table-update.component.html',
})
export class TableUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected tableService: TableService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ table }) => {
      if (table.active) {
        alert('لا يمكن تعديل الصالة وهي مفتوحة');
        this.previousState();
      }
      this.updateForm(table);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const table = this.createFromForm();
    if (table.id !== undefined) {
      this.subscribeToSaveResponse(this.tableService.update(table));
    } else {
      this.subscribeToSaveResponse(this.tableService.create(table));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITable>>): void {
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

  protected updateForm(table: ITable): void {
    this.editForm.patchValue({
      id: table.id,
      name: table.name,
      discount: table.discount,
      totalPrice: table.totalPrice,
    });
  }

  protected createFromForm(): ITable {
    return {
      ...new Table(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      discount: this.editForm.get(['discount'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
    };
  }
}
