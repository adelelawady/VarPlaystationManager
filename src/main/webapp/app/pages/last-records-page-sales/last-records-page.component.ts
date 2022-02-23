import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SheftService } from 'app/home/sheft.service';

@Component({
  selector: 'jhi-last-records-page',
  templateUrl: './last-records-page.component.html',
  styleUrls: ['./last-records-page.component.scss'],
})
export class LastRecordsPageComponent implements OnInit {
  isloading = true;
  sheft: any;
  sliceCount = 8;
  constructor(private sheftService: SheftService, protected http: HttpClient) {}

  ngOnInit(): void {
    this.sheftService.currentUpdated().subscribe(
      (sheft: any) => {
        this.sheft = sheft;

        // this.sheft.tableRecords=this.sheft.tableRecords.slice(0, sliceCount);
        // this.sheft.tableShopsRecords=this.sheft.tableShopsRecords.slice(0, sliceCount);
        // this.sheft.tableTakeAwayRecords=this.sheft.tableTakeAwayRecords.slice(0, sliceCount);
        this.isloading = false;
      },
      () => {
        this.isloading = false;
      }
    );
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

  printDeviceRecored(id: string): void {
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    this.sheftService.printDeviceRecored(id).subscribe();
  }

  printTableRecored(id: string): void {
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    this.sheftService.printTableRecored(id).subscribe();
  }
}
