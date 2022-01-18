import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { TableService } from '../service/table.service';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { combineLatest } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-table',
  templateUrl: './table.component.html',
})
export class TableComponent implements OnInit {
  tables?: any[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ngbPaginationPage = 1;
  sortItem = 0;
  now: Date = new Date();
  from = dayjs().startOf('day'); // set to 12:00 am today
  to = dayjs().endOf('day'); // set to 23:59 pm today
  constructor(
    protected tableService: TableService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  fromDateChanged(date: any): void {
    // eslint-disable-next-line no-console
    this.from = dayjs(date.value);
    this.loadPage();
  }
  toDateChanged(date: any): void {
    // eslint-disable-next-line no-console
    this.to = dayjs(date.value);
    this.loadPage();
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    // eslint-disable-next-line no-console
    this.tableService
      .query(
        {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
        },
        this.sortItem,
        { from: this.from, to: this.to }
      )
      .subscribe(
        (res: HttpResponse<any[]>) => {
          this.isLoading = false;
          // eslint-disable-next-line no-console
          console.log(res);
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: any): string {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return item.id!;
  }

  protected sort(): string[] {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    const result = [this.predicate];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);

      if (pageNumber !== this.page) {
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: any | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/admin/entities/product-stats'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
        },
      });
    }
    this.tables = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
