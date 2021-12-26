import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITakeaway } from '../takeaway.model';

@Component({
  selector: 'jhi-takeaway-detail',
  templateUrl: './takeaway-detail.component.html',
})
export class TakeawayDetailComponent implements OnInit {
  takeaway: ITakeaway | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ takeaway }) => {
      this.takeaway = takeaway;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
