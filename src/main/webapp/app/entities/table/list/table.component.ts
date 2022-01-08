import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITable } from '../table.model';
import { TableService } from '../service/table.service';
import { TableDeleteDialogComponent } from '../delete/table-delete-dialog.component';

@Component({
  selector: 'jhi-table',
  templateUrl: './table.component.html',
})
export class TableComponent implements OnInit {
  tables?: any[];
  isLoading = false;

  constructor(protected tableService: TableService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tableService.query().subscribe(
      (res: HttpResponse<ITable[]>) => {
        this.isLoading = false;
        this.tables = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITable): string {
    return item.id!;
  }

  delete(table: ITable): void {
    const modalRef = this.modalService.open(TableDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.table = table;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
