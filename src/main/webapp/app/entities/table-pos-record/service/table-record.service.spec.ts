import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITableRecord, TableRecord } from '../table-record.model';

import { TableRecordService } from './table-record.service';

describe('TableRecord Service', () => {
  let service: TableRecordService;
  let httpMock: HttpTestingController;
  let elemDefault: ITableRecord;
  let expectedResult: ITableRecord | ITableRecord[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TableRecordService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      totalPrice: 0,
      totalDiscountPrice: 0,
      netTotalPrice: 0,
      discount: 0,
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

    it('should create a TableRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TableRecord()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TableRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          totalPrice: 1,
          totalDiscountPrice: 1,
          netTotalPrice: 1,
          discount: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TableRecord', () => {
      const patchObject = Object.assign(
        {
          totalPrice: 1,
          totalDiscountPrice: 1,
          discount: 1,
        },
        new TableRecord()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TableRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          totalPrice: 1,
          totalDiscountPrice: 1,
          netTotalPrice: 1,
          discount: 1,
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

    it('should delete a TableRecord', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTableRecordToCollectionIfMissing', () => {
      it('should add a TableRecord to an empty array', () => {
        const tableRecord: ITableRecord = { id: 'ABC' };
        expectedResult = service.addTableRecordToCollectionIfMissing([], tableRecord);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tableRecord);
      });

      it('should not add a TableRecord to an array that contains it', () => {
        const tableRecord: ITableRecord = { id: 'ABC' };
        const tableRecordCollection: ITableRecord[] = [
          {
            ...tableRecord,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addTableRecordToCollectionIfMissing(tableRecordCollection, tableRecord);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TableRecord to an array that doesn't contain it", () => {
        const tableRecord: ITableRecord = { id: 'ABC' };
        const tableRecordCollection: ITableRecord[] = [{ id: 'CBA' }];
        expectedResult = service.addTableRecordToCollectionIfMissing(tableRecordCollection, tableRecord);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tableRecord);
      });

      it('should add only unique TableRecord to an array', () => {
        const tableRecordArray: ITableRecord[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '78b2a363-7e26-406b-af98-8a059ee395f2' }];
        const tableRecordCollection: ITableRecord[] = [{ id: 'ABC' }];
        expectedResult = service.addTableRecordToCollectionIfMissing(tableRecordCollection, ...tableRecordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tableRecord: ITableRecord = { id: 'ABC' };
        const tableRecord2: ITableRecord = { id: 'CBA' };
        expectedResult = service.addTableRecordToCollectionIfMissing([], tableRecord, tableRecord2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tableRecord);
        expect(expectedResult).toContain(tableRecord2);
      });

      it('should accept null and undefined values', () => {
        const tableRecord: ITableRecord = { id: 'ABC' };
        expectedResult = service.addTableRecordToCollectionIfMissing([], null, tableRecord, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tableRecord);
      });

      it('should return initial array if no TableRecord is added', () => {
        const tableRecordCollection: ITableRecord[] = [{ id: 'ABC' }];
        expectedResult = service.addTableRecordToCollectionIfMissing(tableRecordCollection, undefined, null);
        expect(expectedResult).toEqual(tableRecordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
