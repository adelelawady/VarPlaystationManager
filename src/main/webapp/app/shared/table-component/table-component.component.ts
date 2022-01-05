import { ChangeDetectionStrategy, ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DevicesSessionsService } from 'app/home/devicesSessions.service';
import { TableService } from '../../entities/table/service/table.service';

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
  @Input() isSelected = false;
  @Input() table: any;

  @Output() deviceSelected = new EventEmitter();
  @Output() tableClicked = new EventEmitter();
  @Output() tableStopped = new EventEmitter();
  @Output() tableStarted = new EventEmitter();
  @Output() tableMovedToDevice = new EventEmitter();

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  constructor(private cd: ChangeDetectorRef, private tableService: TableService, private devicesSessionsService: DevicesSessionsService) {}

  ngOnInit(): void {
    this.getDevicePrice();
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
  moveToDevice(device: any): void {
    this.tableService.moveToDevice(this.table.id, device.id).subscribe((table: any) => {
      this.table = table;
      this.tableMovedToDevice.emit(device);
    });
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
