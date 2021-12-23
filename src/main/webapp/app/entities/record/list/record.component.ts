import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecord } from '../record.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { RecordService } from '../service/record.service';
import { RecordDeleteDialogComponent } from '../delete/record-delete-dialog.component';

@Component({
  selector: 'jhi-record',
  templateUrl: './record.component.html',
})
export class RecordComponent implements OnInit {
  records?: any;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  selectedPeriod = 'today';
  recordsFiltered?: any;
  constructor(
    protected recordService: RecordService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    /* this.recordService
       .query({
         page: pageToLoad - 1,
         size: this.itemsPerPage,
         sort: this.sort(),
       })
       .subscribe(
         (res: HttpResponse<IRecord[]>) => {
           this.isLoading = false;
           this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
         },
         () => {
           this.isLoading = false;
           this.onError();
         }
       );*/

    this.recordService
      .queryFilterd(
        {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        },
        this.selectedPeriod
      )
      .subscribe(
        (recF: any) => {
          this.isLoading = false;
          this.recordsFiltered = recF.body;
          // this.records=recF.body.resultList.content;
          this.onSuccess(recF.body.resultList.content, recF.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }
  loadPeriod(): void {
    this.loadPage(1, true);
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IRecord): string {
    return item.id!;
  }
  fix(h: any): any {
    return Math.floor(h);
  }
  delete(record: IRecord): void {
    const modalRef = this.modalService.open(RecordDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.record = record;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IRecord[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/record'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.records = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
