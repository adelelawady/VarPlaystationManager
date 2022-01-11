jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITableRecord, TableRecord } from '../table-record.model';
import { TableRecordService } from '../service/table-record.service';

import { TableRecordRoutingResolveService } from './table-record-routing-resolve.service';

describe('TableRecord routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TableRecordRoutingResolveService;
  let service: TableRecordService;
  let resultTableRecord: ITableRecord | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TableRecordRoutingResolveService);
    service = TestBed.inject(TableRecordService);
    resultTableRecord = undefined;
  });

  describe('resolve', () => {
    it('should return ITableRecord returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTableRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultTableRecord).toEqual({ id: 'ABC' });
    });

    it('should return new ITableRecord if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTableRecord = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTableRecord).toEqual(new TableRecord());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TableRecord })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTableRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultTableRecord).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
