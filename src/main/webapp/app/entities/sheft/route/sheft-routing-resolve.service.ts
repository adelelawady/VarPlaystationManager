import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISheft, Sheft } from '../sheft.model';
import { SheftService } from '../service/sheft.service';

@Injectable({ providedIn: 'root' })
export class SheftRoutingResolveService implements Resolve<ISheft> {
  constructor(protected service: SheftService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISheft> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sheft: HttpResponse<Sheft>) => {
          if (sheft.body) {
            return of(sheft.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sheft());
  }
}
