jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IShopsOrders, ShopsOrders } from '../shops-orders.model';
import { ShopsOrdersService } from '../service/shops-orders.service';

import { ShopsOrdersRoutingResolveService } from './shops-orders-routing-resolve.service';

describe('ShopsOrders routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ShopsOrdersRoutingResolveService;
  let service: ShopsOrdersService;
  let resultShopsOrders: IShopsOrders | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ShopsOrdersRoutingResolveService);
    service = TestBed.inject(ShopsOrdersService);
    resultShopsOrders = undefined;
  });

  describe('resolve', () => {
    it('should return IShopsOrders returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShopsOrders = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultShopsOrders).toEqual({ id: 'ABC' });
    });

    it('should return new IShopsOrders if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShopsOrders = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultShopsOrders).toEqual(new ShopsOrders());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ShopsOrders })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultShopsOrders = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultShopsOrders).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
