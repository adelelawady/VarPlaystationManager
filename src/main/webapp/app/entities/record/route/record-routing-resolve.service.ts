import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecord, Record } from '../record.model';
import { RecordService } from '../service/record.service';

@Injectable({ providedIn: 'root' })
export class RecordRoutingResolveService implements Resolve<IRecord> {
  constructor(protected service: RecordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((record: HttpResponse<Record>) => {
          if (record.body) {
            return of(record.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Record());
  }
}
