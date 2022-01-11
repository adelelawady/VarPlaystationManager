import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITableRecord, TableRecord } from '../table-record.model';
import { TableRecordService } from '../service/table-record.service';

@Injectable({ providedIn: 'root' })
export class TableRecordRoutingResolveService implements Resolve<any> {
  constructor(protected service: TableRecordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tableRecord: HttpResponse<any>) => {
          if (tableRecord.body) {
            return of(tableRecord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TableRecord());
  }
}
