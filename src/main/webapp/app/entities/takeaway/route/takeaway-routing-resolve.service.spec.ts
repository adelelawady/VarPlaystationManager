jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITakeaway, Takeaway } from '../takeaway.model';
import { TakeawayService } from '../service/takeaway.service';

import { TakeawayRoutingResolveService } from './takeaway-routing-resolve.service';

describe('Takeaway routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TakeawayRoutingResolveService;
  let service: TakeawayService;
  let resultTakeaway: ITakeaway | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TakeawayRoutingResolveService);
    service = TestBed.inject(TakeawayService);
    resultTakeaway = undefined;
  });

  describe('resolve', () => {
    it('should return ITakeaway returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTakeaway = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultTakeaway).toEqual({ id: 'ABC' });
    });

    it('should return new ITakeaway if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTakeaway = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTakeaway).toEqual(new Takeaway());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Takeaway })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTakeaway = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultTakeaway).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
