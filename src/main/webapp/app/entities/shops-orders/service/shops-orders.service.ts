import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShopsOrders, getShopsOrdersIdentifier } from '../shops-orders.model';

export type EntityResponseType = HttpResponse<IShopsOrders>;
export type EntityArrayResponseType = HttpResponse<IShopsOrders[]>;

@Injectable({ providedIn: 'root' })
export class ShopsOrdersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shops-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(shopsOrders: IShopsOrders): Observable<EntityResponseType> {
    return this.http.post<IShopsOrders>(this.resourceUrl, shopsOrders, { observe: 'response' });
  }

  update(shopsOrders: IShopsOrders): Observable<EntityResponseType> {
    return this.http.put<IShopsOrders>(`${this.resourceUrl}/${getShopsOrdersIdentifier(shopsOrders) as string}`, shopsOrders, {
      observe: 'response',
    });
  }

  partialUpdate(shopsOrders: IShopsOrders): Observable<EntityResponseType> {
    return this.http.patch<IShopsOrders>(`${this.resourceUrl}/${getShopsOrdersIdentifier(shopsOrders) as string}`, shopsOrders, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IShopsOrders>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShopsOrders[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addShopsOrdersToCollectionIfMissing(
    shopsOrdersCollection: IShopsOrders[],
    ...shopsOrdersToCheck: (IShopsOrders | null | undefined)[]
  ): IShopsOrders[] {
    const shopsOrders: IShopsOrders[] = shopsOrdersToCheck.filter(isPresent);
    if (shopsOrders.length > 0) {
      const shopsOrdersCollectionIdentifiers = shopsOrdersCollection.map(shopsOrdersItem => getShopsOrdersIdentifier(shopsOrdersItem)!);
      const shopsOrdersToAdd = shopsOrders.filter(shopsOrdersItem => {
        const shopsOrdersIdentifier = getShopsOrdersIdentifier(shopsOrdersItem);
        if (shopsOrdersIdentifier == null || shopsOrdersCollectionIdentifiers.includes(shopsOrdersIdentifier)) {
          return false;
        }
        shopsOrdersCollectionIdentifiers.push(shopsOrdersIdentifier);
        return true;
      });
      return [...shopsOrdersToAdd, ...shopsOrdersCollection];
    }
    return shopsOrdersCollection;
  }
}
