import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITakeaway, Takeaway } from '../takeaway.model';

import { TakeawayService } from './takeaway.service';

describe('Takeaway Service', () => {
  let service: TakeawayService;
  let httpMock: HttpTestingController;
  let elemDefault: ITakeaway;
  let expectedResult: ITakeaway | ITakeaway[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TakeawayService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
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

    it('should create a Takeaway', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Takeaway()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Takeaway', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
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

    it('should partial update a Takeaway', () => {
      const patchObject = Object.assign({}, new Takeaway());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Takeaway', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
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

    it('should delete a Takeaway', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTakeawayToCollectionIfMissing', () => {
      it('should add a Takeaway to an empty array', () => {
        const takeaway: ITakeaway = { id: 'ABC' };
        expectedResult = service.addTakeawayToCollectionIfMissing([], takeaway);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(takeaway);
      });

      it('should not add a Takeaway to an array that contains it', () => {
        const takeaway: ITakeaway = { id: 'ABC' };
        const takeawayCollection: ITakeaway[] = [
          {
            ...takeaway,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addTakeawayToCollectionIfMissing(takeawayCollection, takeaway);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Takeaway to an array that doesn't contain it", () => {
        const takeaway: ITakeaway = { id: 'ABC' };
        const takeawayCollection: ITakeaway[] = [{ id: 'CBA' }];
        expectedResult = service.addTakeawayToCollectionIfMissing(takeawayCollection, takeaway);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(takeaway);
      });

      it('should add only unique Takeaway to an array', () => {
        const takeawayArray: ITakeaway[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '029b3257-a256-4cba-9035-680a07fc5bee' }];
        const takeawayCollection: ITakeaway[] = [{ id: 'ABC' }];
        expectedResult = service.addTakeawayToCollectionIfMissing(takeawayCollection, ...takeawayArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const takeaway: ITakeaway = { id: 'ABC' };
        const takeaway2: ITakeaway = { id: 'CBA' };
        expectedResult = service.addTakeawayToCollectionIfMissing([], takeaway, takeaway2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(takeaway);
        expect(expectedResult).toContain(takeaway2);
      });

      it('should accept null and undefined values', () => {
        const takeaway: ITakeaway = { id: 'ABC' };
        expectedResult = service.addTakeawayToCollectionIfMissing([], null, takeaway, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(takeaway);
      });

      it('should return initial array if no Takeaway is added', () => {
        const takeawayCollection: ITakeaway[] = [{ id: 'ABC' }];
        expectedResult = service.addTakeawayToCollectionIfMissing(takeawayCollection, undefined, null);
        expect(expectedResult).toEqual(takeawayCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
