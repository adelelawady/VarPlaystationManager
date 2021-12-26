import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITable, Table } from '../table.model';
import { TableService } from '../service/table.service';

@Injectable({ providedIn: 'root' })
export class TableRoutingResolveService implements Resolve<ITable> {
  constructor(protected service: TableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((table: HttpResponse<Table>) => {
          if (table.body) {
            return of(table.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Table());
  }
}
