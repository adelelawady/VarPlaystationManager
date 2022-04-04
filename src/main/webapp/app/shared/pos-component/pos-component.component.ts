import { ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DevicesSessionsService } from 'app/home/devicesSessions.service';
import { TableService } from '../../entities/table/service/table.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoryService } from '../../entities/category/service/category.service';
import { ProductService } from '../../../../../../bin/src/main/webapp/app/entities/product/service/product.service';
import { PosService } from './pos.service';
import { PosTableRecordService } from './table-record.service';
import { ITable } from '../../../../../../bin/src/main/webapp/app/entities/table/table.model';

declare const $: any;

@Component({
  selector: 'jhi-pos-component',
  templateUrl: './pos-component.component.html',
  styleUrls: ['./pos-component.component.scss'],
})
export class PosComponentComponent implements OnInit {
  categories: any = [];
  products: any = [];

  isMulti = false;
  print = true;
  discount = 0.0;
  totalPrice = 0;
  devices: any;
  isSingleClick = true;
  disableClick = false;
  tables: any;

  selectedCheck: any = null;

  tableCreated: any = null;

  tableRecords: any = null;

  showOptions = false;
  showCloseButton = false;
  @Input() isSelected = false;
  @Input() tableType = 'cafe';
  @Output() deviceSelected = new EventEmitter();
  @Output() tableClicked = new EventEmitter();
  @Output() tableDoubleClicked = new EventEmitter();
  @Output() tableStopped = new EventEmitter();
  @Output() tableStarted = new EventEmitter();
  @Output() tableMovedToDevice = new EventEmitter();
  @Output() tableMovedToTable = new EventEmitter();

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  constructor(
    private modalService: NgbModal,
    private cd: ChangeDetectorRef,
    private posService: PosService,
    private posTableRecordService: PosTableRecordService,
    private devicesSessionsService: DevicesSessionsService,
    private categoryService: CategoryService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.getAllCategoriesByType(this.tableType);
    this.loadAllPosTablesRecords();

    this.posService.findAll(this.tableType).subscribe((results: any) => {
      if (results.body.length > 0) {
        this.tableCreated = results.body[0];
      }
    });
  }

  createPosAction(): void {
    const tablePos = {
      type: this.tableType.toUpperCase(),
      name: 'TAKE AWAY',
    };
    // eslint-disable-next-line no-console
    console.log(tablePos);
    this.posService.create(tablePos).subscribe(posTableCreated => {
      this.tableCreated = posTableCreated;
    });
  }

  loadAllPosTablesRecords(): void {
    this.posTableRecordService.query(null, this.tableType).subscribe(list => {
      this.tableRecords = list.body;
    });
  }
  selectCheck(check: any): void {
    this.selectedCheck = check;
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

  printTableRecored(id: string): void {
    this.posTableRecordService.printTableRecord(id).subscribe();
  }

  addProductToSelectedDevice(prodId: any): void {
    if (!this.tableCreated) {
      const tablePos = {
        type: this.tableType.toUpperCase(),
        name: 'TAKE AWAY',
      };
      this.posService.create(tablePos).subscribe((posTableCreated: any) => {
        this.tableCreated = posTableCreated.body;
        if (this.tableCreated) {
          this.posService.addProductToTable(this.tableCreated.id, prodId.id).subscribe(tableUpdated => {
            this.tableCreated = tableUpdated;
            this.loadAllPosTablesRecords();
          });
        }
      });
    } else {
      if (this.tableCreated) {
        this.posService.addProductToTable(this.tableCreated.id, prodId.id).subscribe(tableUpdated => {
          this.tableCreated = tableUpdated;
          this.loadAllPosTablesRecords();
        });
      }
    }
  }

  getAllProductsByCategory(categoryId: string): void {
    this.products = [];
    this.productService.queryByCategoryId(categoryId).subscribe(products => {
      this.products = products.body;
    });
  }

  open(content: any): void {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
      result => {
        //this.closeResult = `Closed with: ${result}`;
      },
      reason => {
        //this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      }
    );
  }

  formateMoney(r: any): any {
    // eslint-disable-next-line no-var
    // var formatter = new Intl.NumberFormat('en-US', {
    //   style: 'currency',
    //   currency: 'EGP',
    //   maximumSignificantDigits: 3,
    // These options are needed to round to whole numbers if that's what you want.
    // minimumFractionDigits: 0, // (this suffices for whole numbers, but will print 2500.10 as $2,500.1)
    // maximumFractionDigits: 0, // (causes 2500.99 to be printed as $2,501)
    // });
    //  return formatter.format(r);
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return (
      'EGP ' +
      Number(r)
        .toFixed(2)
        .replace(/\d(?=(\d{3})+\.)/g, '$&,')
    );
  }

  loadAllDevices(): void {
    this.devicesSessionsService.getDevicesSessions().subscribe((devicesFound: any) => {
      this.devices = devicesFound;
    });
  }

  diableClick(): void {
    this.disableClick = true;
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const self = this;
    setTimeout(() => {
      // eslint-disable-next-line @typescript-eslint/no-this-alias

      self.disableClick = false;
    }, 1000);
  }

  cancelCurrentTableCreated(): void {
    // this.tableCreated
    if (!this.tableCreated) {
      return;
    }
    this.posService.delete(this.tableCreated.id).subscribe(() => {
      this.tableCreated = null;
    });
  }
  endSession(): void {
    this.tableCreated.active = false;
    const sessionEnd = {
      print: this.print,
      ordersDiscount: this.discount,
      totalPrice: this.totalPrice,
    };
    this.posService.stopTable(this.tableCreated.id, sessionEnd).subscribe(table => {
      this.tableCreated = null;
      this.cd.markForCheck();
      this.tableStopped.emit(this.tableCreated);
      this.loadAllPosTablesRecords();
    });
  }

  deleteProductFromSelectedDevice(productId: string): void {
    if (this.tableCreated) {
      this.posService.deleteProductFromTable(this.tableCreated.id, productId).subscribe(table => {
        this.tableCreated = table;
      });
    }
  }
}
