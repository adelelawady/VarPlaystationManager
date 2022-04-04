import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class DevicesSessionsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getDevicesSessions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/devices-sessions`, { observe: 'body' });
  }

  getDevicesByActive(active: boolean): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/devices/active/${active.toString()}`, { observe: 'body' });
  }

  stopDeviceSession(deviceId: string, sessionEnd: any): Observable<any[]> {
    return this.http.post<any[]>(`${this.resourceUrl}/stop-session/${deviceId}`, sessionEnd, { observe: 'body' });
  }

  moveDevice(deviceId: string, deviceToId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/device/${deviceId}/session/device/${deviceToId}/move`, { observe: 'body' });
  }

  moveDeviceToMulti(deviceId: string, multi: boolean): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/device/${deviceId}/session/device/multi/${multi.toString()}/move`, {
      observe: 'body',
    });
  }

  startDeviceSession(deviceId: string, sessionStart: any): Observable<any[]> {
    return this.http.post<any[]>(`${this.resourceUrl}/start-session/${deviceId}`, sessionStart, { observe: 'body' });
  }

  addProductToDeviceSession(deviceId: string, productid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/device/${deviceId}/session/product/${productid}/add`, { observe: 'body' });
  }

  deleteProductFromDeviceSession(deviceId: string, productid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/device/${deviceId}/session/product/${productid}/delete`, { observe: 'body' });
  }

  payProductToDeviceSession(deviceId: string, productid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/device/${deviceId}/session/product/${productid}/pay`, { observe: 'body' });
  }

  unPayProductFromDeviceSession(deviceId: string, productid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/device/${deviceId}/session/product/${productid}/unpay`, { observe: 'body' });
  }

  flushOrders(deviceId: string, type: string, print: boolean): Observable<any[]> {
    return this.http.post<any[]>(`${this.resourceUrl}/sessions/flush/orders/device/${deviceId}/${type}`, { print }, { observe: 'body' });
  }

  flushTableOrders(deviceId: string, print: boolean): Observable<any[]> {
    return this.http.post<any[]>(`${this.resourceUrl}/tables/flush/orders/table/${deviceId}`, { print }, { observe: 'body' });
  }
}
