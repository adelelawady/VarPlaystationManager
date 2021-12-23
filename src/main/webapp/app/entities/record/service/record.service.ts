import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecord, getRecordIdentifier } from '../record.model';

export type EntityResponseType = HttpResponse<IRecord>;
export type EntityArrayResponseType = HttpResponse<IRecord[]>;

@Injectable({ providedIn: 'root' })
export class RecordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/records');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(record: IRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(record);
    return this.http
      .post<IRecord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(record: IRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(record);
    return this.http
      .put<IRecord>(`${this.resourceUrl}/${getRecordIdentifier(record) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(record: IRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(record);
    return this.http
      .patch<IRecord>(`${this.resourceUrl}/${getRecordIdentifier(record) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  queryFilterd(req?: any, per?: any): Observable<EntityResponseType> {
    const options = createRequestOption(req);

    const ii = {
      queryType: per,
    };
    return this.http
      .post<any>(`${this.resourceUrl}/filter`, ii, { params: options, observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRecordToCollectionIfMissing(recordCollection: IRecord[], ...recordsToCheck: (IRecord | null | undefined)[]): IRecord[] {
    const records: IRecord[] = recordsToCheck.filter(isPresent);
    if (records.length > 0) {
      const recordCollectionIdentifiers = recordCollection.map(recordItem => getRecordIdentifier(recordItem)!);
      const recordsToAdd = records.filter(recordItem => {
        const recordIdentifier = getRecordIdentifier(recordItem);
        if (recordIdentifier == null || recordCollectionIdentifiers.includes(recordIdentifier)) {
          return false;
        }
        recordCollectionIdentifiers.push(recordIdentifier);
        return true;
      });
      return [...recordsToAdd, ...recordCollection];
    }
    return recordCollection;
  }

  protected convertDateFromClient(record: IRecord): IRecord {
    return Object.assign({}, record, {
      start: record.start?.isValid() ? record.start.toJSON() : undefined,
      end: record.end?.isValid() ? record.end.toJSON() : undefined,
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
      res.body.forEach((record: IRecord) => {
        record.start = record.start ? dayjs(record.start) : undefined;
        record.end = record.end ? dayjs(record.end) : undefined;
      });
    }
    return res;
  }
}
