import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeviceType, getDeviceTypeIdentifier } from '../device-type.model';

export type EntityResponseType = HttpResponse<IDeviceType>;
export type EntityArrayResponseType = HttpResponse<IDeviceType[]>;

@Injectable({ providedIn: 'root' })
export class DeviceTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/device-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deviceType: IDeviceType): Observable<EntityResponseType> {
    return this.http.post<IDeviceType>(this.resourceUrl, deviceType, { observe: 'response' });
  }

  update(deviceType: IDeviceType): Observable<EntityResponseType> {
    return this.http.put<IDeviceType>(`${this.resourceUrl}/${getDeviceTypeIdentifier(deviceType) as string}`, deviceType, {
      observe: 'response',
    });
  }

  partialUpdate(deviceType: IDeviceType): Observable<EntityResponseType> {
    return this.http.patch<IDeviceType>(`${this.resourceUrl}/${getDeviceTypeIdentifier(deviceType) as string}`, deviceType, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IDeviceType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeviceType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeviceTypeToCollectionIfMissing(
    deviceTypeCollection: IDeviceType[],
    ...deviceTypesToCheck: (IDeviceType | null | undefined)[]
  ): IDeviceType[] {
    const deviceTypes: IDeviceType[] = deviceTypesToCheck.filter(isPresent);
    if (deviceTypes.length > 0) {
      const deviceTypeCollectionIdentifiers = deviceTypeCollection.map(deviceTypeItem => getDeviceTypeIdentifier(deviceTypeItem)!);
      const deviceTypesToAdd = deviceTypes.filter(deviceTypeItem => {
        const deviceTypeIdentifier = getDeviceTypeIdentifier(deviceTypeItem);
        if (deviceTypeIdentifier == null || deviceTypeCollectionIdentifiers.includes(deviceTypeIdentifier)) {
          return false;
        }
        deviceTypeCollectionIdentifiers.push(deviceTypeIdentifier);
        return true;
      });
      return [...deviceTypesToAdd, ...deviceTypeCollection];
    }
    return deviceTypeCollection;
  }
}
