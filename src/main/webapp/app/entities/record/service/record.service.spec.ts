import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRecord, Record } from '../record.model';

import { RecordService } from './record.service';

describe('Record Service', () => {
  let service: RecordService;
  let httpMock: HttpTestingController;
  let elemDefault: IRecord;
  let expectedResult: IRecord | IRecord[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecordService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 'AAAAAAA',
      start: currentDate,
      end: currentDate,
      totalPrice: 0,
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

    it('should create a Record', () => {
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

      service.create(new Record()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Record', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          start: currentDate.format(DATE_TIME_FORMAT),
          end: currentDate.format(DATE_TIME_FORMAT),
          totalPrice: 1,
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

    it('should partial update a Record', () => {
      const patchObject = Object.assign(
        {
          start: currentDate.format(DATE_TIME_FORMAT),
          end: currentDate.format(DATE_TIME_FORMAT),
          totalPrice: 1,
        },
        new Record()
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

    it('should return a list of Record', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          start: currentDate.format(DATE_TIME_FORMAT),
          end: currentDate.format(DATE_TIME_FORMAT),
          totalPrice: 1,
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

    it('should delete a Record', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRecordToCollectionIfMissing', () => {
      it('should add a Record to an empty array', () => {
        const record: IRecord = { id: 'ABC' };
        expectedResult = service.addRecordToCollectionIfMissing([], record);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(record);
      });

      it('should not add a Record to an array that contains it', () => {
        const record: IRecord = { id: 'ABC' };
        const recordCollection: IRecord[] = [
          {
            ...record,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addRecordToCollectionIfMissing(recordCollection, record);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Record to an array that doesn't contain it", () => {
        const record: IRecord = { id: 'ABC' };
        const recordCollection: IRecord[] = [{ id: 'CBA' }];
        expectedResult = service.addRecordToCollectionIfMissing(recordCollection, record);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(record);
      });

      it('should add only unique Record to an array', () => {
        const recordArray: IRecord[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '22eeba3a-bfa4-4fd5-91c3-03ce118e9afd' }];
        const recordCollection: IRecord[] = [{ id: 'ABC' }];
        expectedResult = service.addRecordToCollectionIfMissing(recordCollection, ...recordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const record: IRecord = { id: 'ABC' };
        const record2: IRecord = { id: 'CBA' };
        expectedResult = service.addRecordToCollectionIfMissing([], record, record2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(record);
        expect(expectedResult).toContain(record2);
      });

      it('should accept null and undefined values', () => {
        const record: IRecord = { id: 'ABC' };
        expectedResult = service.addRecordToCollectionIfMissing([], null, record, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(record);
      });

      it('should return initial array if no Record is added', () => {
        const recordCollection: IRecord[] = [{ id: 'ABC' }];
        expectedResult = service.addRecordToCollectionIfMissing(recordCollection, undefined, null);
        expect(expectedResult).toEqual(recordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
