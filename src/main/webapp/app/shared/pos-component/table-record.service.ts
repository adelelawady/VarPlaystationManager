import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITableRecord } from '../../../../../../bin/src/main/webapp/app/entities/table-record/table-record.model';
import { getTableRecordIdentifier } from 'app/entities/table-record/table-record.model';

export type EntityResponseType = HttpResponse<ITableRecord>;
export type EntityArrayResponseType = HttpResponse<ITableRecord[]>;

@Injectable({ providedIn: 'root' })
export class PosTableRecordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pos/table-records');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tableRecord: ITableRecord): Observable<EntityResponseType> {
    return this.http.post<ITableRecord>(this.resourceUrl, tableRecord, { observe: 'response' });
  }

  update(tableRecord: ITableRecord): Observable<EntityResponseType> {
    return this.http.put<ITableRecord>(`${this.resourceUrl}/${getTableRecordIdentifier(tableRecord) as string}`, tableRecord, {
      observe: 'response',
    });
  }

  partialUpdate(tableRecord: ITableRecord): Observable<EntityResponseType> {
    return this.http.patch<ITableRecord>(`${this.resourceUrl}/${getTableRecordIdentifier(tableRecord) as string}`, tableRecord, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ITableRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllByCreatedDate(type: string): Observable<EntityResponseType> {
    return this.http.get<ITableRecord>(`${this.resourceUrl}/${type}/allbycreated`, { observe: 'response' });
  }

  query(req?: any, type?: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return this.http.get<ITableRecord[]>(this.resourceUrl + '/type/' + type, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  printTableRecord(id: string): Observable<EntityResponseType> {
    return this.http.get<ITableRecord>(`${this.resourceUrl}/print/${id}`, { observe: 'response' });
  }

  addTableRecordToCollectionIfMissing(
    tableRecordCollection: ITableRecord[],
    ...tableRecordsToCheck: (ITableRecord | null | undefined)[]
  ): ITableRecord[] {
    const tableRecords: ITableRecord[] = tableRecordsToCheck.filter(isPresent);
    if (tableRecords.length > 0) {
      const tableRecordCollectionIdentifiers = tableRecordCollection.map(tableRecordItem => getTableRecordIdentifier(tableRecordItem)!);
      const tableRecordsToAdd = tableRecords.filter(tableRecordItem => {
        const tableRecordIdentifier = getTableRecordIdentifier(tableRecordItem);
        if (tableRecordIdentifier == null || tableRecordCollectionIdentifiers.includes(tableRecordIdentifier)) {
          return false;
        }
        tableRecordCollectionIdentifiers.push(tableRecordIdentifier);
        return true;
      });
      return [...tableRecordsToAdd, ...tableRecordCollection];
    }
    return tableRecordCollection;
  }
}
