import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';

import { ISheft } from '../sheft.model';

@Component({
  selector: 'jhi-sheft-detail',
  templateUrl: './sheft-detail.component.html',
})
export class SheftDetailComponent implements OnInit {
  sheft: any | null = null;

  // We use this trigger because fetching the list of persons can be quite long,
  // thus we ensure the data is fetched before rendering
  dtTrigger: Subject<any> = new Subject<any>();

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sheft }) => {
      this.sheft = sheft;
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

  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }

  previousState(): void {
    window.history.back();
  }
}
