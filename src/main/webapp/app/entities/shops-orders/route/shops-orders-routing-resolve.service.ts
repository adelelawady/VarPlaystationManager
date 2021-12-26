import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShopsOrders, ShopsOrders } from '../shops-orders.model';
import { ShopsOrdersService } from '../service/shops-orders.service';

@Injectable({ providedIn: 'root' })
export class ShopsOrdersRoutingResolveService implements Resolve<IShopsOrders> {
  constructor(protected service: ShopsOrdersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShopsOrders> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((shopsOrders: HttpResponse<ShopsOrders>) => {
          if (shopsOrders.body) {
            return of(shopsOrders.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ShopsOrders());
  }
}
