import {
  Component,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  EventEmitter,
  OnDestroy,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
} from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { TableService } from '../../../entities/table/service/table.service';
declare const $: any;
@Component({
  selector: 'jhi-tables-tap-component',
  templateUrl: './tables-tap-component.component.html',
  styleUrls: ['./tables-tap-component.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TablesTapComponentComponent implements OnInit, OnDestroy {
  selectedTable: any;
  tables: any[] | null = null;

  @Input() tableType = 'table'; // table , takeaway , shops
  @Output() tableSelected = new EventEmitter();
  @Output() tableDoubleClicked = new EventEmitter();
  @Output() tableCheckOut = new EventEmitter();
  completeBtn = false;

  ordersButtons: any[] = [
    {
      icon: 'minus',
      style: 'danger',
      hint: 'مسح',
      type: 'danger',
      // disabled: (this.selectedDevice ? (this.selectedDevice.session?.paidOrdersPrice>0):false),
      action: 'deleteOrderItem',
    },
    {
      icon: 'plus',
      style: 'italic',
      hint: 'اضافة',
      type: 'success',
      action: 'addOrderItem',
    },
    {
      icon: 'money',
      style: 'italic',
      hint: 'حساب',
      type: 'warning',
      action: 'payOrderItem',
    },
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

  private eventsSubscription: Subscription | undefined;

  // eslint-disable-next-line @typescript-eslint/member-ordering
  // eslint-disable-next-line @typescript-eslint/member-ordering
  @Input()
  events!: Observable<void>;

  constructor(private cd: ChangeDetectorRef, private tableService: TableService) {}

  ngOnDestroy(): void {
    if (!this.eventsSubscription) {
      return;
    }
    this.eventsSubscription.unsubscribe();
  }
  ngOnInit(): void {
    this.loadAllTables();
    this.eventsSubscription = this.events.subscribe(() => {
      this.loadAllTables();
      this.cd.markForCheck();
    });
  }
  loadAllTables(): void {
    this.tableService.findAll(this.tableType).subscribe(tables => {
      this.tables = tables.body;
      if (this.selectedTable && this.tables) {
        this.selectedTable = this.tables.filter(t => t.id === this.selectedTable.id)[0];
        this.cd.markForCheck();
      }
    });
  }
  selectTable(table: any): void {
    this.selectedTable = table;
    this.tableSelected.emit(table);
  }

  toObjectKeys(any: any): any {
    return Object.keys(any);
  }
  orderItemClick(e: any, prodId: any): void {
    // eslint-disable-next-line no-console
    console.log(e.itemData.action);
    switch (e.itemData.action) {
      case 'deleteOrderItem':
        this.deleteProductFromSelectedDevice(prodId);
        break;
      case 'addOrderItem':
        this.addProductFromSelectedDevice(prodId);
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

  getProductPrice(prod: any): any {
    switch (this.tableType) {
      case 'table':
        return prod.price;
      case 'takeaway':
        return prod.takeawayPrice;
      case 'shops':
        return prod.shopsPrice;
      default:
        break;
    }
  }
  tableMovedToDevice(device: any): void {
    this.loadAllTables();
  }
  onTableDoubleClicked(table: any): void {
    this.selectedTable = table;
    this.tableDoubleClicked.emit(table);
  }
  // eslint-disable-next-line @typescript-eslint/adjacent-overload-signatures
  deleteProductFromSelectedDevice(prodId: string): void {
    this.tableService.deleteProductFromTable(this.selectedTable.id, prodId).subscribe(table => {
      this.selectedTable = table;
      this.loadAllTables();
      this.cd.markForCheck();
    });
  }

  // eslint-disable-next-line @typescript-eslint/adjacent-overload-signatures
  addProductFromSelectedDevice(prodId: string): void {
    this.tableService.addProductToTable(this.selectedTable.id, prodId).subscribe(table => {
      this.selectedTable = table;
      this.loadAllTables();
      this.cd.markForCheck();
    });
  }

  payProductFromSelectedDevice(productId: string): void {
    if (this.selectedTable) {
      this.tableService.payProductToDeviceSession(this.selectedTable.id, productId).subscribe(deivce => {
        this.selectedTable = deivce;
        this.loadAllTables();
        this.cd.markForCheck();
      });
    }
  }

  unPayProductFromSelectedDevice(productId: string): void {
    if (this.selectedTable) {
      this.tableService.unPayProductFromDeviceSession(this.selectedTable.id, productId).subscribe(deivce => {
        this.selectedTable = deivce;
        this.loadAllTables();
        this.cd.markForCheck();
      });
    }
  }

  completeSelectedDevicePaidOrdersPayment(print: boolean): void {
    if (this.selectedTable) {
      this.completeBtn = true;
      this.tableService.completePaiedSessionOrdersPayment(this.selectedTable.id, print).subscribe(
        deivce => {
          this.selectedTable = deivce;
          this.completeBtn = false;
          this.loadAllTables();
          this.cd.markForCheck();
        },
        () => {
          this.completeBtn = false;
        }
      );
    }
  }

  tableMovedToTable(table: any): void {
    this.loadAllTables();
  }
}
