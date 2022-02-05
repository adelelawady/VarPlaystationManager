import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecord } from '../record.model';

@Component({
  selector: 'jhi-record-detail',
  templateUrl: './record-detail.component.html',
})
export class RecordDetailComponent implements OnInit {
  record: any | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ record }) => {
      this.record = record;
    });
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

  previousState(): void {
    window.history.back();
  }
}
