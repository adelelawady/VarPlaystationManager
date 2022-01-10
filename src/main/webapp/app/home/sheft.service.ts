import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class SheftService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  start(): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/sheft/start`, { observe: 'body' });
  }
  current(): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/sheft/current`, { observe: 'body' });
  }

  currentUpdated(): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/sheft/current/updated`, { observe: 'body' });
  }
  stop(): Observable<any[]> {
    return this.http.get<any[]>(`${this.resourceUrl}/sheft/stop`, { observe: 'body' });
  }
}
