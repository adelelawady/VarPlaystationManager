import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;

  categoriesCollection: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    price: [],
    category: [],
    takeawayPrice: [],
    shopsPrice: [],
  });

  constructor(
    protected productService: ProductService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.updateForm(product);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  trackCategoryById(index: number, item: ICategory): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: any): void {
    this.editForm.patchValue({
      id: product.id,
      name: product.name,
      price: product.price,
      category: product.category,
      takeawayPrice: product.takeawayPrice,
      shopsPrice: product.shopsPrice,
    });

    this.categoriesCollection = this.categoryService.addCategoryToCollectionIfMissing(this.categoriesCollection, product.category);
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query({ filter: 'product-is-null' })
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesCollection = categories));
  }

  protected createFromForm(): any {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      price: this.editForm.get(['price'])!.value,
      category: this.editForm.get(['category'])!.value,
      takeawayPrice: this.editForm.get(['takeawayPrice'])!.value,
      shopsPrice: this.editForm.get(['shopsPrice'])!.value,
    };
  }
}
