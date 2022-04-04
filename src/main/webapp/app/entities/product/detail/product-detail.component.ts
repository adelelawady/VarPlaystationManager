import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';

@Component({
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
})
export class ProductDetailComponent implements OnInit {
  product: any | null = null;
  addNew = false;

  constructor(protected activatedRoute: ActivatedRoute, protected productService: ProductService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
    });
  }

  removeSub(subId: string): void {
    this.productService.removeSubItem(this.product.id, subId).subscribe();
    location.reload();
  }
  previousState(): void {
    window.history.back();
  }
}
