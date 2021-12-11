import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeviceType, DeviceType } from '../device-type.model';

import { DeviceTypeService } from './device-type.service';

describe('DeviceType Service', () => {
  let service: DeviceTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IDeviceType;
  let expectedResult: IDeviceType | IDeviceType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeviceTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      name: 'AAAAAAA',
      pricePerHour: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DeviceType', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DeviceType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DeviceType', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          pricePerHour: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DeviceType', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          pricePerHour: 1,
        },
        new DeviceType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DeviceType', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          pricePerHour: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DeviceType', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDeviceTypeToCollectionIfMissing', () => {
      it('should add a DeviceType to an empty array', () => {
        const deviceType: IDeviceType = { id: 'ABC' };
        expectedResult = service.addDeviceTypeToCollectionIfMissing([], deviceType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceType);
      });

      it('should not add a DeviceType to an array that contains it', () => {
        const deviceType: IDeviceType = { id: 'ABC' };
        const deviceTypeCollection: IDeviceType[] = [
          {
            ...deviceType,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addDeviceTypeToCollectionIfMissing(deviceTypeCollection, deviceType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DeviceType to an array that doesn't contain it", () => {
        const deviceType: IDeviceType = { id: 'ABC' };
        const deviceTypeCollection: IDeviceType[] = [{ id: 'CBA' }];
        expectedResult = service.addDeviceTypeToCollectionIfMissing(deviceTypeCollection, deviceType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceType);
      });

      it('should add only unique DeviceType to an array', () => {
        const deviceTypeArray: IDeviceType[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '099a4b5a-e6ef-4bd6-afb1-2f1cb0fbc122' }];
        const deviceTypeCollection: IDeviceType[] = [{ id: 'ABC' }];
        expectedResult = service.addDeviceTypeToCollectionIfMissing(deviceTypeCollection, ...deviceTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deviceType: IDeviceType = { id: 'ABC' };
        const deviceType2: IDeviceType = { id: 'CBA' };
        expectedResult = service.addDeviceTypeToCollectionIfMissing([], deviceType, deviceType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deviceType);
        expect(expectedResult).toContain(deviceType2);
      });

      it('should accept null and undefined values', () => {
        const deviceType: IDeviceType = { id: 'ABC' };
        expectedResult = service.addDeviceTypeToCollectionIfMissing([], null, deviceType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deviceType);
      });

      it('should return initial array if no DeviceType is added', () => {
        const deviceTypeCollection: IDeviceType[] = [{ id: 'ABC' }];
        expectedResult = service.addDeviceTypeToCollectionIfMissing(deviceTypeCollection, undefined, null);
        expect(expectedResult).toEqual(deviceTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
