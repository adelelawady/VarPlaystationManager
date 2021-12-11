import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecord } from '../record.model';

@Component({
  selector: 'jhi-record-detail',
  templateUrl: './record-detail.component.html',
})
export class RecordDetailComponent implements OnInit {
  record: IRecord | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ record }) => {
      this.record = record;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
