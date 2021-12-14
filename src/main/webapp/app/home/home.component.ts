import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { DevicesSessionsService } from './devicesSessions.service';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ProductService } from 'app/entities/product/service/product.service';
declare const $: any;
@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  opend = true;
  account: Account | null = null;
  devices: any[] | null = null;
  categories: any = [];
  products: any = [];
  selectedDevice: any;
  private readonly destroy$ = new Subject<void>();
  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private accountService: AccountService,
    private router: Router,
    private devicesSessionService: DevicesSessionsService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.loadAllDevices();
    this.getAllCategories();
  }

  loadAllDevices(): void {
    this.devicesSessionService.getDevicesSessions().subscribe(devicesFound => {
      this.devices = devicesFound;
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  openOrdersPanle(): void {
    if (!this.isOrdersOpened()) {
      $('.ng-sidebar-header').click();
    }
  }

  getAllCategories(): void {
    this.products = [];
    this.categoryService.query().subscribe(categories => {
      this.categories = categories.body;
    });
  }

  getAllProductsByCategory(categoryId: string): void {
    this.productService.queryByCategoryId(categoryId).subscribe(products => {
      this.products = products.body;
    });
  }

  isOrdersOpened(): any {
    return $('.ng-sidebar_opened').length;
  }
  closeOrdersPanle(): void {
    if (this.isOrdersOpened()) {
      $('.ng-sidebar-header').click();
    }
  }

  addProductToSelectedDevice(productId: string): void {
    this.devicesSessionService.addProductToDeviceSession(this.selectedDevice.id, productId).subscribe(deivce => {
      this.loadAllDevices();
      this.selectedDevice = deivce;
    });
  }
}
