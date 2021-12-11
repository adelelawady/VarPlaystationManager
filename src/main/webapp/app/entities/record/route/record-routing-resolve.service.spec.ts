jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRecord, Record } from '../record.model';
import { RecordService } from '../service/record.service';

import { RecordRoutingResolveService } from './record-routing-resolve.service';

describe('Record routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RecordRoutingResolveService;
  let service: RecordService;
  let resultRecord: IRecord | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RecordRoutingResolveService);
    service = TestBed.inject(RecordService);
    resultRecord = undefined;
  });

  describe('resolve', () => {
    it('should return IRecord returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultRecord).toEqual({ id: 'ABC' });
    });

    it('should return new IRecord if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRecord = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRecord).toEqual(new Record());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Record })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultRecord).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
