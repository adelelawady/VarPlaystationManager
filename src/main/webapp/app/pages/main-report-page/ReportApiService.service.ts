import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class ReportApiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getMainReport(fromTo: any): Observable<any[]> {
    return this.http.post<any[]>(`${this.resourceUrl}/report/main-report`, fromTo, { observe: 'body' });
  }
}
