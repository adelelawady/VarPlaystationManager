import { Component, OnInit } from '@angular/core';
import { SheftService } from '../../home/sheft.service';

@Component({
  selector: 'jhi-single-sheft-report',
  templateUrl: './single-sheft-report.component.html',
  styleUrls: ['./single-sheft-report.component.scss'],
})
export class SingleSheftReportComponent implements OnInit {
  sheft: any;
  constructor(private sheftService: SheftService) {}

  startSheft(): void {
    this.sheftService.start().subscribe(() => {
      this.sheftService.current().subscribe((sheft: any) => {
        this.sheft = sheft;
      });
    });
  }

  stopSheft(): void {
    this.sheftService.stop().subscribe(() => {
      this.sheftService.current().subscribe((sheft: any) => {
        this.sheft = sheft;
        location.reload();
      });
    });
  }
  ngOnInit(): void {
    this.sheftService.currentUpdated().subscribe((sheft: any) => {
      this.sheft = sheft;
    });
  }
}
