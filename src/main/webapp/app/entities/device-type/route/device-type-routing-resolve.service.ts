import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeviceType, DeviceType } from '../device-type.model';
import { DeviceTypeService } from '../service/device-type.service';

@Injectable({ providedIn: 'root' })
export class DeviceTypeRoutingResolveService implements Resolve<IDeviceType> {
  constructor(protected service: DeviceTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeviceType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deviceType: HttpResponse<DeviceType>) => {
          if (deviceType.body) {
            return of(deviceType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeviceType());
  }
}
