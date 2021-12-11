jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDevice, Device } from '../device.model';
import { DeviceService } from '../service/device.service';

import { DeviceRoutingResolveService } from './device-routing-resolve.service';

describe('Device routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DeviceRoutingResolveService;
  let service: DeviceService;
  let resultDevice: IDevice | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DeviceRoutingResolveService);
    service = TestBed.inject(DeviceService);
    resultDevice = undefined;
  });

  describe('resolve', () => {
    it('should return IDevice returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDevice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultDevice).toEqual({ id: 'ABC' });
    });

    it('should return new IDevice if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDevice = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDevice).toEqual(new Device());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Device })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDevice = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultDevice).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
