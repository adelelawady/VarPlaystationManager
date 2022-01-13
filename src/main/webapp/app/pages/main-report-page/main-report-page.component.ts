import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReportApiService } from './ReportApiService.service';

@Component({
  selector: 'jhi-main-report-page',
  templateUrl: './main-report-page.component.html',
  styleUrls: ['./main-report-page.component.scss'],
})
export class MainReportPageComponent implements OnInit {
  from: any;
  to: any;
  fromAsDate: any;
  toAsDate: any;
  report: any;
  loading = false;
  constructor(public datepipe: DatePipe, private reportApiService: ReportApiService) {}
  formateMoney(r: any): any {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return 'EGP ' + r.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
  }
  today(): void {
    this.loading = true;
    this.fromAsDate = this.datepipe.transform(Date.now(), 'yyyy-MM-dd');
    this.toAsDate = this.datepipe.transform(Date.now(), 'yyyy-MM-dd');
    this.from = Date.now();
    this.to = Date.now();
    const someDateVar = Date.now();
    const fromTo = {
      from: someDateVar,
      to: someDateVar,
    };
    this.reportApiService.getMainReport(fromTo).subscribe(response => {
      this.report = response;
      this.loading = false;
    });
  }

  yesterday(): void {
    this.loading = true;
    const date = new Date();

    date.setDate(date.getDate() - 1);
    this.fromAsDate = this.datepipe.transform(date, 'yyyy-MM-dd');
    this.toAsDate = this.datepipe.transform(date, 'yyyy-MM-dd');
    this.from = date;
    this.to = date;
    const someDateVar = date;
    const fromTo = {
      from: someDateVar,
      to: someDateVar,
    };
    this.reportApiService.getMainReport(fromTo).subscribe(response => {
      this.report = response;
      this.loading = false;
    });
  }

  lastWeek(): void {
    this.loading = true;
    const date = new Date();
    const datenow = new Date();
    date.setDate(date.getDate() - 7);
    this.fromAsDate = this.datepipe.transform(date, 'yyyy-MM-dd');
    this.toAsDate = this.datepipe.transform(datenow, 'yyyy-MM-dd');
    this.from = date;
    this.to = datenow;
    const fromTo = {
      from: date,
      to: datenow,
    };
    this.reportApiService.getMainReport(fromTo).subscribe(response => {
      this.report = response;
      this.loading = false;
    });
  }

  currentMonth(): void {
    this.loading = true;
    const daten = new Date();

    const firstDay = new Date(daten.getFullYear(), daten.getMonth(), 1);

    const lastDay =
      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
      new Date(daten.getFullYear(), daten.getMonth() + 1, 0);

    const date = firstDay;
    const datenow = lastDay;
    this.fromAsDate = this.datepipe.transform(date, 'yyyy-MM-dd');
    this.toAsDate = this.datepipe.transform(datenow, 'yyyy-MM-dd');
    this.from = date;
    this.to = datenow;
    const fromTo = {
      from: date,
      to: datenow,
    };
    this.reportApiService.getMainReport(fromTo).subscribe(response => {
      this.report = response;
      this.loading = false;
    });
  }

  pervMonth(): void {
    this.loading = true;
    const daten = new Date();

    const firstDay = new Date(daten.getFullYear(), daten.getMonth() - 1, 1);

    const lastDay =
      // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
      new Date(daten.getFullYear(), daten.getMonth(), 0);

    const date = firstDay;
    const datenow = lastDay;
    this.fromAsDate = this.datepipe.transform(date, 'yyyy-MM-dd');
    this.toAsDate = this.datepipe.transform(datenow, 'yyyy-MM-dd');
    this.from = date;
    this.to = datenow;
    const fromTo = {
      from: date,
      to: datenow,
    };

    this.reportApiService.getMainReport(fromTo).subscribe(response => {
      this.report = response;
      this.loading = false;
    });
  }

  ngOnInit(): void {
    this.today();
  }
  parseDate(date: any): any {
    return Date.parse(date);
  }

  reload(): void {
    this.loading = true;
    const fromTo = {
      from: Date.parse(this.fromAsDate),
      to: Date.parse(this.toAsDate),
    };

    this.reportApiService.getMainReport(fromTo).subscribe(response => {
      this.report = response;
      this.loading = false;
    });
  }
}
