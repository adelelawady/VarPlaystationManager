import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IShopsOrders } from '../shops-orders.model';
import { ShopsOrdersService } from '../service/shops-orders.service';
import { ShopsOrdersDeleteDialogComponent } from '../delete/shops-orders-delete-dialog.component';

@Component({
  selector: 'jhi-shops-orders',
  templateUrl: './shops-orders.component.html',
})
export class ShopsOrdersComponent implements OnInit {
  shopsOrders?: IShopsOrders[];
  isLoading = false;

  constructor(protected shopsOrdersService: ShopsOrdersService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.shopsOrdersService.query().subscribe(
      (res: HttpResponse<IShopsOrders[]>) => {
        this.isLoading = false;
        this.shopsOrders = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IShopsOrders): string {
    return item.id!;
  }

  delete(shopsOrders: IShopsOrders): void {
    const modalRef = this.modalService.open(ShopsOrdersDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.shopsOrders = shopsOrders;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
