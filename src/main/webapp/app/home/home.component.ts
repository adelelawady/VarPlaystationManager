import { Component, OnInit, OnDestroy, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { DevicesSessionsService } from './devicesSessions.service';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ProductService } from 'app/entities/product/service/product.service';
import { TableService } from 'app/entities/table/service/table.service';
declare const $: any;
@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
  formatter = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'EGP',

    // These options are needed to round to whole numbers if that's what you want.
    //minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
    //maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
  });
  opend = true;
  account: Account | null = null;
  devices: any[] | null = null;
  tables: any[] | null = null;
  categories: any = [];
  products: any = [];
  selectedDevice: any;
  selectedTable: any;

  tapDevices = true;
  tapTables = false;
  tapTakeaway = false;
  tapShops = false;

  private readonly destroy$ = new Subject<void>();
  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private accountService: AccountService,
    private router: Router,
    private tableService: TableService,
    private devicesSessionService: DevicesSessionsService
  ) {}
  ngAfterViewInit(): void {
    this.closeOrdersPanle();
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.loadAllDevices();
    this.getAllCategories();
  }

  selectTap(tabname: any): void {
    this.selectedDevice = null;
    this.selectedTable = null;
    switch (tabname) {
      case 'tapDevices':
        this.tapDevices = true;
        this.tapTables = false;
        this.tapTakeaway = false;
        this.tapShops = false;
        this.loadAllDevices();
        break;
      case 'tapTables':
        this.tapDevices = false;
        this.tapTables = true;
        this.tapTakeaway = false;
        this.tapShops = false;
        this.loadAllTables();
        break;
      case 'tapTakeaway':
        this.tapDevices = false;
        this.tapTables = false;
        this.tapTakeaway = true;
        this.tapShops = false;
        break;

      case 'tapShops':
        this.tapDevices = false;
        this.tapTables = false;
        this.tapTakeaway = false;
        this.tapShops = true;
        break;
      default:
        break;
    }
  }
  loadAllDevices(): void {
    this.devicesSessionService.getDevicesSessions().subscribe(devicesFound => {
      this.devices = devicesFound;
    });
  }

  loadAllTables(): void {
    this.tableService.query().subscribe(tables => {
      this.tables = tables.body;
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }
  formateMoney(r: any): any {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return 'EGP ' + r.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  openOrdersPanle(): void {
    // eslint-disable-next-line no-console
    console.log(this.selectedDevice);
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
    this.products = [];
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
    if (this.selectedDevice) {
      this.devicesSessionService.addProductToDeviceSession(this.selectedDevice.id, productId).subscribe(deivce => {
        this.selectedDevice = deivce;
        this.loadAllDevices();
      });
    }
    if (this.selectedTable) {
      this.tableService.addProductToTable(this.selectedTable.id, productId).subscribe(table => {
        this.selectedTable = table;
        this.loadAllTables();
      });
    }
  }

  deleteProductFromSelectedDevice(productId: string): void {
    if (this.selectedDevice) {
      this.devicesSessionService.deleteProductFromDeviceSession(this.selectedDevice.id, productId).subscribe(deivce => {
        this.selectedDevice = deivce;
        this.loadAllDevices();
      });
    }
    if (this.selectedTable) {
      this.tableService.deleteProductFromTable(this.selectedTable.id, productId).subscribe(table => {
        this.selectedTable = table;
        this.loadAllTables();
      });
    }
  }
}
