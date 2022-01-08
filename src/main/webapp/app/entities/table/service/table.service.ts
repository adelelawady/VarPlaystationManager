import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITable, getTableIdentifier } from '../table.model';

export type EntityResponseType = HttpResponse<any>;
export type EntityArrayResponseType = HttpResponse<any[]>;

@Injectable({ providedIn: 'root' })
export class TableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(table: ITable): Observable<EntityResponseType> {
    return this.http.post<any>(this.resourceUrl, table, { observe: 'response' });
  }

  update(table: any): Observable<EntityResponseType> {
    return this.http.put<any>(`${this.resourceUrl}/${getTableIdentifier(table) as string}`, table, { observe: 'response' });
  }

  partialUpdate(table: ITable): Observable<EntityResponseType> {
    return this.http.patch<any>(`${this.resourceUrl}/${getTableIdentifier(table) as string}`, table, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<any[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  findAll(type: string): Observable<EntityArrayResponseType> {
    return this.http.get<any[]>(this.resourceUrl + '/' + type + '/getall', { observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  stopTable(tableId: string, sessionEnd: any): Observable<any[]> {
    return this.http.post<any[]>(`${this.resourceUrl}/stop-session/${tableId}`, sessionEnd, { observe: 'body' });
  }

  startTable(tableId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/start-session/${tableId}`, { observe: 'body' });
  }

  addProductToTable(tableId: string, productid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/${tableId}/products/${productid}/add`, { observe: 'body' });
  }

  deleteProductFromTable(tableId: string, productid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/${tableId}/products/${productid}/delete`, { observe: 'body' });
  }

  moveToDevice(tableId: string, deviceId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/${tableId}/devices/${deviceId}/move`, { observe: 'body' });
  }

  moveToTable(tableId: string, tableIdToMoveTO: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/${tableId}/tables/${tableIdToMoveTO}/move`, { observe: 'body' });
  }

  addTableToCollectionIfMissing(tableCollection: ITable[], ...tablesToCheck: (ITable | null | undefined)[]): ITable[] {
    const tables: ITable[] = tablesToCheck.filter(isPresent);
    if (tables.length > 0) {
      const tableCollectionIdentifiers = tableCollection.map(tableItem => getTableIdentifier(tableItem)!);
      const tablesToAdd = tables.filter(tableItem => {
        const tableIdentifier = getTableIdentifier(tableItem);
        if (tableIdentifier == null || tableCollectionIdentifiers.includes(tableIdentifier)) {
          return false;
        }
        tableCollectionIdentifiers.push(tableIdentifier);
        return true;
      });
      return [...tablesToAdd, ...tableCollection];
    }
    return tableCollection;
  }
}
