import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISheft, Sheft } from '../sheft.model';

import { SheftService } from './sheft.service';

describe('Sheft Service', () => {
  let service: SheftService;
  let httpMock: HttpTestingController;
  let elemDefault: ISheft;
  let expectedResult: ISheft | ISheft[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SheftService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 'AAAAAAA',
      user: 'AAAAAAA',
      start: currentDate,
      end: currentDate,
      totalNetPrice: 0,
      totalDiscount: 0,
      totalNetPriceAfterDiscount: 0,
      totalNetPriceAfterDiscountSystem: 0,
      totalNetPriceDevices: 0,
      totalNetUserPriceDevices: 0,
      totalDiscountPriceDevices: 0,
      totalPriceTimeDevices: 0,
      totalPriceOrdersDevices: 0,
      totalNetPriceTables: 0,
      totalDiscountPriceTables: 0,
      totalNetPriceAfterDiscountTables: 0,
      totalNetPriceTakeaway: 0,
      totalDiscountPriceTakeaway: 0,
      totalNetPriceAfterDiscountTakeaway: 0,
      totalNetPriceShops: 0,
      totalDiscountPriceShops: 0,
      totalNetPriceAfterDiscountShops: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          start: currentDate.format(DATE_TIME_FORMAT),
          end: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Sheft', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
          start: currentDate.format(DATE_TIME_FORMAT),
          end: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          start: currentDate,
          end: currentDate,
        },
        returnedFromService
      );

      service.create(new Sheft()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sheft', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          user: 'BBBBBB',
          start: currentDate.format(DATE_TIME_FORMAT),
          end: currentDate.format(DATE_TIME_FORMAT),
          totalNetPrice: 1,
          totalDiscount: 1,
          totalNetPriceAfterDiscount: 1,
          totalNetPriceAfterDiscountSystem: 1,
          totalNetPriceDevices: 1,
          totalNetUserPriceDevices: 1,
          totalDiscountPriceDevices: 1,
          totalPriceTimeDevices: 1,
          totalPriceOrdersDevices: 1,
          totalNetPriceTables: 1,
          totalDiscountPriceTables: 1,
          totalNetPriceAfterDiscountTables: 1,
          totalNetPriceTakeaway: 1,
          totalDiscountPriceTakeaway: 1,
          totalNetPriceAfterDiscountTakeaway: 1,
          totalNetPriceShops: 1,
          totalDiscountPriceShops: 1,
          totalNetPriceAfterDiscountShops: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          start: currentDate,
          end: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sheft', () => {
      const patchObject = Object.assign(
        {
          user: 'BBBBBB',
          start: currentDate.format(DATE_TIME_FORMAT),
          end: currentDate.format(DATE_TIME_FORMAT),
          totalNetPrice: 1,
          totalDiscount: 1,
          totalNetPriceAfterDiscount: 1,
          totalNetPriceDevices: 1,
          totalNetUserPriceDevices: 1,
          totalDiscountPriceDevices: 1,
          totalNetPriceTables: 1,
          totalNetPriceAfterDiscountTakeaway: 1,
          totalDiscountPriceShops: 1,
        },
        new Sheft()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          start: currentDate,
          end: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sheft', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          user: 'BBBBBB',
          start: currentDate.format(DATE_TIME_FORMAT),
          end: currentDate.format(DATE_TIME_FORMAT),
          totalNetPrice: 1,
          totalDiscount: 1,
          totalNetPriceAfterDiscount: 1,
          totalNetPriceAfterDiscountSystem: 1,
          totalNetPriceDevices: 1,
          totalNetUserPriceDevices: 1,
          totalDiscountPriceDevices: 1,
          totalPriceTimeDevices: 1,
          totalPriceOrdersDevices: 1,
          totalNetPriceTables: 1,
          totalDiscountPriceTables: 1,
          totalNetPriceAfterDiscountTables: 1,
          totalNetPriceTakeaway: 1,
          totalDiscountPriceTakeaway: 1,
          totalNetPriceAfterDiscountTakeaway: 1,
          totalNetPriceShops: 1,
          totalDiscountPriceShops: 1,
          totalNetPriceAfterDiscountShops: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          start: currentDate,
          end: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Sheft', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSheftToCollectionIfMissing', () => {
      it('should add a Sheft to an empty array', () => {
        const sheft: ISheft = { id: 'ABC' };
        expectedResult = service.addSheftToCollectionIfMissing([], sheft);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sheft);
      });

      it('should not add a Sheft to an array that contains it', () => {
        const sheft: ISheft = { id: 'ABC' };
        const sheftCollection: ISheft[] = [
          {
            ...sheft,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addSheftToCollectionIfMissing(sheftCollection, sheft);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sheft to an array that doesn't contain it", () => {
        const sheft: ISheft = { id: 'ABC' };
        const sheftCollection: ISheft[] = [{ id: 'CBA' }];
        expectedResult = service.addSheftToCollectionIfMissing(sheftCollection, sheft);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sheft);
      });

      it('should add only unique Sheft to an array', () => {
        const sheftArray: ISheft[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '9119a368-2b3e-4657-b9f0-ee6c0cb0f48b' }];
        const sheftCollection: ISheft[] = [{ id: 'ABC' }];
        expectedResult = service.addSheftToCollectionIfMissing(sheftCollection, ...sheftArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sheft: ISheft = { id: 'ABC' };
        const sheft2: ISheft = { id: 'CBA' };
        expectedResult = service.addSheftToCollectionIfMissing([], sheft, sheft2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sheft);
        expect(expectedResult).toContain(sheft2);
      });

      it('should accept null and undefined values', () => {
        const sheft: ISheft = { id: 'ABC' };
        expectedResult = service.addSheftToCollectionIfMissing([], null, sheft, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sheft);
      });

      it('should return initial array if no Sheft is added', () => {
        const sheftCollection: ISheft[] = [{ id: 'ABC' }];
        expectedResult = service.addSheftToCollectionIfMissing(sheftCollection, undefined, null);
        expect(expectedResult).toEqual(sheftCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
