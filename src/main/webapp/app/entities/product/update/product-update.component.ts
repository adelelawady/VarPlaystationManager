import { Component, OnInit, Input } from '@angular/core';
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
  @Input() addSub = false;
  @Input() addSubParent: any;
  categoriesCollection: ICategory[] = [];
  categories: any[] = [];
  editForm = this.fb.group({
    id: [],
    name: [],
    price: [],
    mediumPrice: [],
    largePrice: [],
    category: [],
    takeawayPrice: [],
    shopsPrice: [],
    enName: [],
  });

  constructor(
    protected productService: ProductService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    if (this.addSub) {
      this.loadRelationshipsOptions();
      return;
    }
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

    if (this.addSub) {
      this.productService.addSubItem(this.addSubParent, product).subscribe();
      location.reload();
      // return;
    }
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  trackCategoryById(index: number, item: ICategory): string {
    return item.id!;
  }

  getAllCategories(): void {
    this.categories = [];
    this.categoryService.findAll().subscribe((categories: any) => {
      this.categories = categories.body;
    });
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
      mediumPrice: product.mediumPrice,
      largePrice: product.largePrice,
      category: product.category,
      takeawayPrice: product.takeawayPrice,
      shopsPrice: product.shopsPrice,
      enName: product.enName,
    });

    this.categoriesCollection = this.categoryService.addCategoryToCollectionIfMissing(this.categoriesCollection, product.category);
  }

  protected loadRelationshipsOptions(): void {
    this.getAllCategories();
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
      mediumPrice: this.editForm.get(['mediumPrice'])!.value,
      largePrice: this.editForm.get(['largePrice'])!.value,
      category: this.editForm.get(['category'])!.value,
      takeawayPrice: this.editForm.get(['takeawayPrice'])!.value,
      shopsPrice: this.editForm.get(['shopsPrice'])!.value,
      enName: this.editForm.get(['enName'])!.value,
    };
  }
}
