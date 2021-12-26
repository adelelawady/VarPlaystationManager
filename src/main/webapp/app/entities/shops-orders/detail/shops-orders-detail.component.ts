import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShopsOrders } from '../shops-orders.model';

@Component({
  selector: 'jhi-shops-orders-detail',
  templateUrl: './shops-orders-detail.component.html',
})
export class ShopsOrdersDetailComponent implements OnInit {
  shopsOrders: IShopsOrders | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shopsOrders }) => {
      this.shopsOrders = shopsOrders;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
