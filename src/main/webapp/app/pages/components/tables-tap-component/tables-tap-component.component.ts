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
import { DevicesSessionsService } from '../../../home/devicesSessions.service';
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
  printFlushOrders = true;

  @Input() tableType = 'table'; // table , takeaway , shops
  @Output() tableSelected = new EventEmitter();
  @Output() tableDoubleClicked = new EventEmitter();
  @Output() tableCheckOut = new EventEmitter();

  private eventsSubscription: Subscription | undefined;

  // eslint-disable-next-line @typescript-eslint/member-ordering
  // eslint-disable-next-line @typescript-eslint/member-ordering
  @Input()
  events!: Observable<void>;

  constructor(private cd: ChangeDetectorRef, private tableService: TableService, private devicesSessionService: DevicesSessionsService) {}

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

  sendFlushOrders(): void {
    this.devicesSessionService.flushTableOrders(this.selectedTable.id, this.printFlushOrders).subscribe(device => {
      this.selectedTable = device;
      this.loadAllTables();
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

  tableMovedToTable(table: any): void {
    this.loadAllTables();
  }
}
