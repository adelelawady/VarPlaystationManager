jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISheft, Sheft } from '../sheft.model';
import { SheftService } from '../service/sheft.service';

import { SheftRoutingResolveService } from './sheft-routing-resolve.service';

describe('Sheft routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SheftRoutingResolveService;
  let service: SheftService;
  let resultSheft: ISheft | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SheftRoutingResolveService);
    service = TestBed.inject(SheftService);
    resultSheft = undefined;
  });

  describe('resolve', () => {
    it('should return ISheft returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSheft = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultSheft).toEqual({ id: 'ABC' });
    });

    it('should return new ISheft if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSheft = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSheft).toEqual(new Sheft());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Sheft })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSheft = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultSheft).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
