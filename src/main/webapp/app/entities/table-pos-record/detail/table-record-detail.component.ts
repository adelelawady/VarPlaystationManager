import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITableRecord } from '../table-record.model';

@Component({
  selector: 'jhi-table-record-detail',
  templateUrl: './table-record-detail.component.html',
})
export class TableRecordDetailComponent implements OnInit {
  tableRecord: any | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tableRecord }) => {
      this.tableRecord = tableRecord;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
