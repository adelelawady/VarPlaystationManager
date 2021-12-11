jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDeviceType, DeviceType } from '../device-type.model';
import { DeviceTypeService } from '../service/device-type.service';

import { DeviceTypeRoutingResolveService } from './device-type-routing-resolve.service';

describe('DeviceType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DeviceTypeRoutingResolveService;
  let service: DeviceTypeService;
  let resultDeviceType: IDeviceType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DeviceTypeRoutingResolveService);
    service = TestBed.inject(DeviceTypeService);
    resultDeviceType = undefined;
  });

  describe('resolve', () => {
    it('should return IDeviceType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDeviceType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultDeviceType).toEqual({ id: 'ABC' });
    });

    it('should return new IDeviceType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDeviceType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDeviceType).toEqual(new DeviceType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DeviceType })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDeviceType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith('ABC');
      expect(resultDeviceType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
