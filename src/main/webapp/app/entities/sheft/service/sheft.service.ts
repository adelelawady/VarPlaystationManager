import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISheft, getSheftIdentifier } from '../sheft.model';

export type EntityResponseType = HttpResponse<ISheft>;
export type EntityArrayResponseType = HttpResponse<ISheft[]>;

@Injectable({ providedIn: 'root' })
export class SheftService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shefts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sheft: ISheft): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sheft);
    return this.http
      .post<ISheft>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sheft: ISheft): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sheft);
    return this.http
      .put<ISheft>(`${this.resourceUrl}/${getSheftIdentifier(sheft) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sheft: ISheft): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sheft);
    return this.http
      .patch<ISheft>(`${this.resourceUrl}/${getSheftIdentifier(sheft) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<ISheft>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISheft[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSheftToCollectionIfMissing(sheftCollection: ISheft[], ...sheftsToCheck: (ISheft | null | undefined)[]): ISheft[] {
    const shefts: ISheft[] = sheftsToCheck.filter(isPresent);
    if (shefts.length > 0) {
      const sheftCollectionIdentifiers = sheftCollection.map(sheftItem => getSheftIdentifier(sheftItem)!);
      const sheftsToAdd = shefts.filter(sheftItem => {
        const sheftIdentifier = getSheftIdentifier(sheftItem);
        if (sheftIdentifier == null || sheftCollectionIdentifiers.includes(sheftIdentifier)) {
          return false;
        }
        sheftCollectionIdentifiers.push(sheftIdentifier);
        return true;
      });
      return [...sheftsToAdd, ...sheftCollection];
    }
    return sheftCollection;
  }

  protected convertDateFromClient(sheft: ISheft): ISheft {
    return Object.assign({}, sheft, {
      start: sheft.start?.isValid() ? sheft.start.toJSON() : undefined,
      end: sheft.end?.isValid() ? sheft.end.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.start = res.body.start ? dayjs(res.body.start) : undefined;
      res.body.end = res.body.end ? dayjs(res.body.end) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sheft: ISheft) => {
        sheft.start = sheft.start ? dayjs(sheft.start) : undefined;
        sheft.end = sheft.end ? dayjs(sheft.end) : undefined;
      });
    }
    return res;
  }
}
