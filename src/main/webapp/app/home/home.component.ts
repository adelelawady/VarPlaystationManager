import { PlaygroundDevicesSessionsService } from './playgrounddevicesSessions.service';
import { Component, OnInit, OnDestroy, AfterViewInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { DevicesSessionsService } from './devicesSessions.service';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ProductService } from 'app/entities/product/service/product.service';
import { TableService } from 'app/entities/table/service/table.service';
import { TakeawayService } from 'app/entities/takeaway/service/takeaway.service';
import { ShopsOrdersService } from 'app/entities/shops-orders/service/shops-orders.service';
import { SheftService } from './sheft.service';
declare const $: any;
declare const document: any;
@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
  now: Date = new Date();
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
  playGrounds: any[] | null = null;
  tables: any[] | null = null;
  categories: any = [];
  products: any = [];
  selectedDevice: any;
  selectedTable: any;

  printFlushOrders = true;

  showProducts = true;

  currentSheft: any | undefined;
  eventReloadTables: Subject<void> = new Subject<void>();

  tapDevices = true;
  tapTables = false;

  tapCafe = false;

  tapRes = false;

  tapMarket = false;

  tapGames = false;

  tapPlayGround = false;

  tapTakeaway = false;
  tapShops = false;

  takeawayOrders: any = [];

  shopsOrders: any = [];

  disablePanelClose = false;

  disablePanelOpen = false;

  showCheckOut = false;

  ordersButtons: any[] = [
    {
      icon: 'minus',
      style: 'danger',
      hint: 'مسح',
      type: 'danger',
      action: 'deleteOrderItem',
    },
    /* {
      icon: 'money',
      style: 'italic',
      hint: 'حساب',
      type: 'success',
      action: 'payOrderItem'
    }*/
  ];

  paidOrdersButtons: any[] = [
    {
      icon: 'undo',
      style: 'back',
      hint: 'الغاء الدفع',
      type: 'danger',
      action: 'unPayOrderItem',
    },
  ];
  private readonly destroy$ = new Subject<void>();
  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private accountService: AccountService,
    private router: Router,
    private takeawayService: TakeawayService,
    private tableService: TableService,
    private shopsOrdersService: ShopsOrdersService,
    private devicesSessionService: DevicesSessionsService,
    private playgroundDevicesSessionsService: PlaygroundDevicesSessionsService
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

  callReloadTables(): void {
    this.eventReloadTables.next();
  }

  onTableSelectionChange(table: any): void {
    this.selectedTable = table;
  }
  selectTap(tabname: any): void {
    this.selectedDevice = null;
    this.selectedTable = null;
    this.showCheckOut = false;
    switch (tabname) {
      case 'tapDevices':
        this.getAllCategories();
        this.showProducts = true;
        this.tapDevices = true;
        this.tapTables = false;
        this.tapTakeaway = false;
        this.tapShops = false;
        this.tapCafe = false;

        this.tapRes = false;

        this.tapMarket = false;

        this.tapGames = false;

        this.tapPlayGround = false;
        this.loadAllDevices();
        break;
      case 'tapTables':
        this.getAllCategories();
        this.showProducts = true;

        this.tapDevices = false;
        this.tapTables = true;
        this.tapTakeaway = false;
        this.tapShops = false;
        this.tapCafe = false;

        this.tapRes = false;

        this.tapMarket = false;

        this.tapGames = false;

        this.tapPlayGround = false;
        this.callReloadTables();
        break;
      case 'tapTakeaway':
        this.getAllCategories();
        this.showProducts = true;

        this.tapDevices = false;
        this.tapTables = false;
        this.tapTakeaway = true;
        this.tapShops = false;
        this.tapCafe = false;

        this.tapRes = false;

        this.tapMarket = false;

        this.tapGames = false;

        this.tapPlayGround = false;
        this.callReloadTables();
        break;

      case 'tapShops':
        this.getAllCategories();
        this.showProducts = true;

        this.tapDevices = false;
        this.tapTables = false;
        this.tapTakeaway = false;
        this.tapShops = true;
        this.tapCafe = false;

        this.tapRes = false;

        this.tapMarket = false;

        this.tapGames = false;

        this.tapPlayGround = false;
        this.callReloadTables();
        break;
      case 'tapCafe':
        this.getAllCategoriesByType('cafe');
        this.showProducts = false;

        this.tapDevices = false;
        this.tapTables = false;
        this.tapTakeaway = false;
        this.tapShops = false;
        this.tapCafe = true;

        this.tapRes = false;

        this.tapMarket = false;

        this.tapGames = false;

        this.tapPlayGround = false;
        this.callReloadTables();
        break;

      case 'tapRes':
        this.getAllCategoriesByType('res');
        this.showProducts = false;
        this.tapDevices = false;
        this.tapTables = false;
        this.tapTakeaway = false;
        this.tapShops = false;
        this.tapCafe = false;

        this.tapRes = true;

        this.tapMarket = false;

        this.tapGames = false;

        this.tapPlayGround = false;
        this.callReloadTables();
        break;
      case 'tapMarket':
        this.getAllCategoriesByType('market');
        this.showProducts = false;
        this.tapDevices = false;
        this.tapTables = false;
        this.tapTakeaway = false;
        this.tapShops = false;
        this.tapCafe = false;

        this.tapRes = false;

        this.tapMarket = true;

        this.tapGames = false;

        this.tapPlayGround = false;
        this.callReloadTables();
        break;
      case 'tapGames':
        this.getAllCategories();
        this.showProducts = true;
        this.tapDevices = false;
        this.tapTables = false;
        this.tapTakeaway = false;
        this.tapShops = false;
        this.tapCafe = false;

        this.tapRes = false;

        this.tapMarket = false;

        this.tapGames = true;

        this.tapPlayGround = false;
        this.callReloadTables();
        break;
      case 'tapPlayGround':
        this.getAllCategories();
        this.loadAllPlayGrounds();
        this.showProducts = true;
        this.tapDevices = false;
        this.tapTables = false;
        this.tapTakeaway = false;
        this.tapShops = false;
        this.tapCafe = false;

        this.tapRes = false;

        this.tapMarket = false;

        this.tapGames = false;

        this.tapPlayGround = true;
        this.callReloadTables();
        break;
      default:
        break;
    }
  }
  loadAllDevices(): void {
    this.devicesSessionService.getDevicesSessions().subscribe((devicesFound: any) => {
      this.devices = devicesFound;
      // eslint-disable-next-line eqeqeq
      if (this.selectedDevice && this.selectedDevice.id === devicesFound.id) {
        this.selectedDevice = devicesFound;
      }
    });
  }

  loadAllPlayGrounds(): void {
    this.playgroundDevicesSessionsService.getDevicesSessions().subscribe((devicesFound: any) => {
      this.playGrounds = devicesFound;
    });
  }

  deviceCheckoutStopped(): void {
    this.selectedDevice = null;
    this.closeOrdersPanle();
    this.showCheckOut = false;
    if (this.tapPlayGround) {
      this.loadAllPlayGrounds();
    } else {
      this.loadAllDevices();
    }
  }

  deviceCheckoutStarted(event: any): void {
    this.showCheckOut = false;
    this.selectedDevice = event;

    if (this.tapPlayGround) {
      this.loadAllPlayGrounds();
    } else {
      this.loadAllDevices();
    }
  }

  sendFlushOrders(): void {
    if (!this.tapTables) {
      let type = 'device';
      if (this.tapDevices) {
        type = 'device';
      }
      if (this.tapPlayGround) {
        type = 'playground';
      }
      this.devicesSessionService.flushOrders(this.selectedDevice.id, type, this.printFlushOrders).subscribe(device => {
        this.selectedDevice = device;
        this.loadAllDevices();
      });
    } else {
      this.devicesSessionService.flushTableOrders(this.selectedTable.id, this.printFlushOrders).subscribe(device => {
        this.selectedTable = device;
        this.callReloadTables();
      });
    }
    ///tables/flush/orders/table/{tableId}
  }

  tableMovedToDevice(device: any): void {
    this.selectTap('tapDevices');
    this.selectedDevice = device;
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const self = this;
    setTimeout(() => {
      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
      $('#device-' + device.id + '-Orderselector').click();
      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
      document.getElementById('#device-' + device.id + '-Orderselector').scrollIntoView();
      self.disablePanelClose = false;
      self.closeOrdersPanle();
    }, 1000);
  }

  tableMovedToTable(table: any): void {
    this.selectedTable = table;
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const self = this;
    setTimeout(() => {
      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
      $('#table-' + table.id + 'selector').click();
      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
      document.getElementById('#table-' + table.id + 'selector').scrollIntoView();
      self.disablePanelClose = false;
      self.closeOrdersPanle();
    }, 500);
  }

  login(): void {
    this.router.navigate(['/login']);
  }
  formateMoney(r: any): any {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return (
      'EGP ' +
      Number(r)
        .toFixed(2)
        .replace(/\d(?=(\d{3})+\.)/g, '$&,')
    );
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  openOrdersPanle(): void {
    this.disablePanelClose = true;
    if (!this.isOrdersOpened()) {
      $('.ng-sidebar-header').click();
      // eslint-disable-next-line @typescript-eslint/no-this-alias
      const self = this;
      setTimeout(() => {
        self.disablePanelClose = false;
      }, 500);
    }
  }

  getAllTakeawayOrders(): void {
    this.takeawayService.query().subscribe(takeaways => {
      this.takeawayOrders = takeaways.body;
    });
  }

  getAllShopsOrders(): void {
    this.shopsOrdersService.query().subscribe(shopsOrders => {
      this.shopsOrders = shopsOrders.body;
    });
  }

  getAllCategories(): void {
    this.products = [];
    this.categories = [];
    this.categoryService.findAll().subscribe(categories => {
      this.categories = categories.body;
    });
  }

  getAllCategoriesByType(type: string): void {
    this.products = [];
    this.categories = [];
    this.categoryService.findAllByType(type).subscribe(categories => {
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
    if (this.isOrdersOpened() && !this.disablePanelClose) {
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

        this.callReloadTables();
      });
    }

    if (this.tapPlayGround) {
      this.playgroundDevicesSessionsService.addProductToDeviceSession(this.selectedDevice.id, productId).subscribe(table => {
        this.selectedDevice = table;
        // this.loadAllDevices();
        this.loadAllPlayGrounds();
      });
    }
    if (this.tapTakeaway) {
      this.takeawayService.createFromOrderProduct(productId).subscribe(takeaway => {
        this.getAllTakeawayOrders();
      });
    }

    if (this.tapShops) {
      this.shopsOrdersService.createFromOrderProduct(productId).subscribe(takeaway => {
        this.getAllShopsOrders();
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
        this.callReloadTables();
      });
    }

    if (this.tapPlayGround) {
      this.playgroundDevicesSessionsService.deleteProductFromDeviceSession(this.selectedDevice.id, productId).subscribe(table => {
        this.selectedDevice = table;
        // this.loadAllDevices();
        this.loadAllPlayGrounds();
      });
    }
  }

  orderItemClick(e: any, prodId: any): void {
    // eslint-disable-next-line no-console
    console.log(e.itemData.action);
    switch (e.itemData.action) {
      case 'deleteOrderItem':
        this.deleteProductFromSelectedDevice(prodId);
        break;
      case 'payOrderItem':
        this.payProductFromSelectedDevice(prodId);
        break;
      case 'unPayOrderItem':
        this.unPayProductFromSelectedDevice(prodId);
        break;
      default:
        break;
    }
  }

  toObjectKeys(any: any): any {
    return Object.keys(any);
  }
  payProductFromSelectedDevice(productId: string): void {
    if (this.selectedDevice) {
      this.devicesSessionService.payProductToDeviceSession(this.selectedDevice.id, productId).subscribe(deivce => {
        this.selectedDevice = deivce;
        this.loadAllDevices();
      });
    }
  }

  unPayProductFromSelectedDevice(productId: string): void {
    if (this.selectedDevice) {
      this.devicesSessionService.unPayProductFromDeviceSession(this.selectedDevice.id, productId).subscribe(deivce => {
        this.selectedDevice = deivce;
        this.loadAllDevices();
      });
    }
  }

  deleteTakeaway(id: any): void {
    this.takeawayService.delete(id).subscribe(take => {
      this.getAllTakeawayOrders();
    });
  }

  deleteShopOrder(id: any): void {
    this.shopsOrdersService.delete(id).subscribe(take => {
      this.getAllShopsOrders();
    });
  }
}
