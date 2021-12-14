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

  stopDeviceSession(deviceId: string, sessionEnd: any): Observable<any[]> {
    return this.http.post<any[]>(`${this.resourceUrl}/stop-session/${deviceId}`, sessionEnd, { observe: 'body' });
  }

  startDeviceSession(deviceId: string, sessionStart: any): Observable<any[]> {
    return this.http.post<any[]>(`${this.resourceUrl}/start-session/${deviceId}`, sessionStart, { observe: 'body' });
  }

  addProductToDeviceSession(deviceId: string, productid: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/device/${deviceId}/session/product/${productid}/add`, { observe: 'body' });
  }
}
