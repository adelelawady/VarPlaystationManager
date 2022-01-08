import { ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DevicesSessionsService } from 'app/home/devicesSessions.service';
import { TableService } from '../../entities/table/service/table.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

declare const $: any;

@Component({
  selector: 'jhi-table-component',
  templateUrl: './table-component.component.html',
  styleUrls: ['./table-component.component.scss'],
})
export class TableComponentComponent implements OnInit {
  isMulti = false;
  print = true;
  discount = 0.0;
  totalPrice = 0;
  devices: any;
  isSingleClick = true;
  disableClick = false;
  tables: any;

  showOptions = false;
  showCloseButton = false;
  @Input() isSelected = false;
  @Input() table: any;
  @Input() tableType = 'table';
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
    private tableService: TableService,
    private devicesSessionsService: DevicesSessionsService
  ) {}

  ngOnInit(): void {
    this.getDevicePrice();
    $(document).ready(function () {
      $('.dropdown-submenu a.test').on('click', function (e: any) {
        $('.dropdown-submenu a.test').next('ul').toggle();
        e.stopPropagation();
        e.preventDefault();
      });
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

  singleClick(): void {
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const self = this;
    if (this.disableClick) {
      // eslint-disable-next-line @typescript-eslint/no-this-alias

      setTimeout(() => {
        self.disableClick = false;
      }, 1000);
      return;
    }

    self.isSingleClick = true;
    setTimeout(() => {
      if (self.isSingleClick) {
        self.deviceClicked();
      }
    }, 250);
  }
  dblClick(): void {
    // eslint-disable-next-line @typescript-eslint/no-this-alias
    const self = this;
    if (this.disableClick) {
      // eslint-disable-next-line @typescript-eslint/no-this-alias

      setTimeout(() => {
        self.disableClick = false;
      }, 1000);
      return;
    }
    this.isSingleClick = false;
    this.getDevicePrice();
    this.tableDoubleClicked.emit(this.table);
  }

  openCheckoutModal(): void {
    this.showOptions = false;
    if (!this.table.active) {
      this.singleClick();
      return;
    }
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    $('#modal' + this.table.id).modal();
  }

  formateMoney(r: any): any {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return 'EGP ' + r.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
  }
  getDevicePrice(): void {
    if (this.table?.active) {
      if (this.discount && this.discount <= 100 && this.discount > 0) {
        const resdis = Math.round(((100 - this.discount) * this.table.totalPrice) / 100);
        this.totalPrice = resdis;
      } else {
        this.totalPrice = this.table.totalPrice;
      }
    }
  }
  loadAllDevices(): void {
    this.devicesSessionsService.getDevicesSessions().subscribe((devicesFound: any) => {
      this.devices = devicesFound;
    });
  }
  loadAllTables(): void {
    this.tableService.findAll(this.tableType).subscribe(tables => {
      this.tables = tables.body;
    });
  }
  moveToDevice(device: any): void {
    this.tableService.moveToDevice(this.table.id, device.id).subscribe((table: any) => {
      this.table = table;
      this.tableMovedToDevice.emit(device);
    });
  }

  moveToTable(table: any): void {
    this.tableService.moveToTable(this.table.id, table.id).subscribe((tableValue: any) => {
      this.table = tableValue;
      this.tableMovedToTable.emit(table);
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
  deviceClicked(): void {
    this.getDevicePrice();
    this.tableClicked.emit(this.table);
  }
  endSession(): void {
    this.table.active = false;
    const sessionEnd = {
      print: this.print,
      ordersDiscount: this.discount,
      totalPrice: this.totalPrice,
    };
    this.tableService.stopTable(this.table.id, sessionEnd).subscribe(table => {
      this.table = table;
      this.cd.markForCheck();
      this.tableStopped.emit(this.table);
    });
  }
}
