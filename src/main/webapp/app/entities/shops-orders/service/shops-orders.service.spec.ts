import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IShopsOrders, ShopsOrders } from '../shops-orders.model';

import { ShopsOrdersService } from './shops-orders.service';

describe('ShopsOrders Service', () => {
  let service: ShopsOrdersService;
  let httpMock: HttpTestingController;
  let elemDefault: IShopsOrders;
  let expectedResult: IShopsOrders | IShopsOrders[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShopsOrdersService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      name: 'AAAAAAA',
      totalPrice: 0,
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

    it('should create a ShopsOrders', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ShopsOrders()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShopsOrders', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          totalPrice: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShopsOrders', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          totalPrice: 1,
        },
        new ShopsOrders()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShopsOrders', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          name: 'BBBBBB',
          totalPrice: 1,
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

    it('should delete a ShopsOrders', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addShopsOrdersToCollectionIfMissing', () => {
      it('should add a ShopsOrders to an empty array', () => {
        const shopsOrders: IShopsOrders = { id: 'ABC' };
        expectedResult = service.addShopsOrdersToCollectionIfMissing([], shopsOrders);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shopsOrders);
      });

      it('should not add a ShopsOrders to an array that contains it', () => {
        const shopsOrders: IShopsOrders = { id: 'ABC' };
        const shopsOrdersCollection: IShopsOrders[] = [
          {
            ...shopsOrders,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addShopsOrdersToCollectionIfMissing(shopsOrdersCollection, shopsOrders);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShopsOrders to an array that doesn't contain it", () => {
        const shopsOrders: IShopsOrders = { id: 'ABC' };
        const shopsOrdersCollection: IShopsOrders[] = [{ id: 'CBA' }];
        expectedResult = service.addShopsOrdersToCollectionIfMissing(shopsOrdersCollection, shopsOrders);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shopsOrders);
      });

      it('should add only unique ShopsOrders to an array', () => {
        const shopsOrdersArray: IShopsOrders[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'b37ad4b9-f19e-491a-9d41-da04a7d5b7d5' }];
        const shopsOrdersCollection: IShopsOrders[] = [{ id: 'ABC' }];
        expectedResult = service.addShopsOrdersToCollectionIfMissing(shopsOrdersCollection, ...shopsOrdersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shopsOrders: IShopsOrders = { id: 'ABC' };
        const shopsOrders2: IShopsOrders = { id: 'CBA' };
        expectedResult = service.addShopsOrdersToCollectionIfMissing([], shopsOrders, shopsOrders2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shopsOrders);
        expect(expectedResult).toContain(shopsOrders2);
      });

      it('should accept null and undefined values', () => {
        const shopsOrders: IShopsOrders = { id: 'ABC' };
        expectedResult = service.addShopsOrdersToCollectionIfMissing([], null, shopsOrders, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shopsOrders);
      });

      it('should return initial array if no ShopsOrders is added', () => {
        const shopsOrdersCollection: IShopsOrders[] = [{ id: 'ABC' }];
        expectedResult = service.addShopsOrdersToCollectionIfMissing(shopsOrdersCollection, undefined, null);
        expect(expectedResult).toEqual(shopsOrdersCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
