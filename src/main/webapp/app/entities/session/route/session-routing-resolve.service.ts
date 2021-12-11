import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISession, Session } from '../session.model';
import { SessionService } from '../service/session.service';

@Injectable({ providedIn: 'root' })
export class SessionRoutingResolveService implements Resolve<ISession> {
  constructor(protected service: SessionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISession> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((session: HttpResponse<Session>) => {
          if (session.body) {
            return of(session.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Session());
  }
}
