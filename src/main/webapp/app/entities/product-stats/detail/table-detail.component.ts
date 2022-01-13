import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-table-detail',
  templateUrl: './table-detail.component.html',
})
export class TableDetailComponent implements OnInit {
  table: any | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ table }) => {
      this.table = table;
    });
  }
  previousState(): void {
    window.history.back();
  }
}
