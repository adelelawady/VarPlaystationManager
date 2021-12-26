import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITakeaway } from '../takeaway.model';
import { TakeawayService } from '../service/takeaway.service';
import { TakeawayDeleteDialogComponent } from '../delete/takeaway-delete-dialog.component';

@Component({
  selector: 'jhi-takeaway',
  templateUrl: './takeaway.component.html',
})
export class TakeawayComponent implements OnInit {
  takeaways?: ITakeaway[];
  isLoading = false;

  constructor(protected takeawayService: TakeawayService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.takeawayService.query().subscribe(
      (res: HttpResponse<ITakeaway[]>) => {
        this.isLoading = false;
        this.takeaways = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITakeaway): string {
    return item.id!;
  }

  delete(takeaway: ITakeaway): void {
    const modalRef = this.modalService.open(TakeawayDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.takeaway = takeaway;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
