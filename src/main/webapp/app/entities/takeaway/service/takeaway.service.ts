import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITakeaway, getTakeawayIdentifier } from '../takeaway.model';

export type EntityResponseType = HttpResponse<any>;
export type EntityArrayResponseType = HttpResponse<any[]>;

@Injectable({ providedIn: 'root' })
export class TakeawayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/takeaways');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(takeaway: ITakeaway): Observable<EntityResponseType> {
    return this.http.post<ITakeaway>(this.resourceUrl, takeaway, { observe: 'response' });
  }

  update(takeaway: any): Observable<EntityResponseType> {
    return this.http.put<any>(`${this.resourceUrl}/${getTakeawayIdentifier(takeaway) as string}`, takeaway, { observe: 'response' });
  }

  partialUpdate(takeaway: ITakeaway): Observable<EntityResponseType> {
    return this.http.patch<any>(`${this.resourceUrl}/${getTakeawayIdentifier(takeaway) as string}`, takeaway, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  createFromOrderProduct(orderId: string): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceUrl}/create/product/${orderId}`, { observe: 'response' });
  }
  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITakeaway[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTakeawayToCollectionIfMissing(takeawayCollection: ITakeaway[], ...takeawaysToCheck: (ITakeaway | null | undefined)[]): ITakeaway[] {
    const takeaways: ITakeaway[] = takeawaysToCheck.filter(isPresent);
    if (takeaways.length > 0) {
      const takeawayCollectionIdentifiers = takeawayCollection.map(takeawayItem => getTakeawayIdentifier(takeawayItem)!);
      const takeawaysToAdd = takeaways.filter(takeawayItem => {
        const takeawayIdentifier = getTakeawayIdentifier(takeawayItem);
        if (takeawayIdentifier == null || takeawayCollectionIdentifiers.includes(takeawayIdentifier)) {
          return false;
        }
        takeawayCollectionIdentifiers.push(takeawayIdentifier);
        return true;
      });
      return [...takeawaysToAdd, ...takeawayCollection];
    }
    return takeawayCollection;
  }
}
