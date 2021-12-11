import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISession, Session } from '../session.model';

import { SessionService } from './session.service';

describe('Session Service', () => {
  let service: SessionService;
  let httpMock: HttpTestingController;
  let elemDefault: ISession;
  let expectedResult: ISession | ISession[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SessionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 'AAAAAAA',
      start: currentDate,
      reserved: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          start: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Session', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
          start: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          start: currentDate,
        },
        returnedFromService
      );

      service.create(new Session()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Session', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          start: currentDate.format(DATE_TIME_FORMAT),
          reserved: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          start: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Session', () => {
      const patchObject = Object.assign(
        {
          start: currentDate.format(DATE_TIME_FORMAT),
        },
        new Session()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          start: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Session', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          start: currentDate.format(DATE_TIME_FORMAT),
          reserved: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          start: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Session', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSessionToCollectionIfMissing', () => {
      it('should add a Session to an empty array', () => {
        const session: ISession = { id: 'ABC' };
        expectedResult = service.addSessionToCollectionIfMissing([], session);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(session);
      });

      it('should not add a Session to an array that contains it', () => {
        const session: ISession = { id: 'ABC' };
        const sessionCollection: ISession[] = [
          {
            ...session,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addSessionToCollectionIfMissing(sessionCollection, session);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Session to an array that doesn't contain it", () => {
        const session: ISession = { id: 'ABC' };
        const sessionCollection: ISession[] = [{ id: 'CBA' }];
        expectedResult = service.addSessionToCollectionIfMissing(sessionCollection, session);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(session);
      });

      it('should add only unique Session to an array', () => {
        const sessionArray: ISession[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'd16e3c9e-3eaa-452a-a02f-b9f664f2f170' }];
        const sessionCollection: ISession[] = [{ id: 'ABC' }];
        expectedResult = service.addSessionToCollectionIfMissing(sessionCollection, ...sessionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const session: ISession = { id: 'ABC' };
        const session2: ISession = { id: 'CBA' };
        expectedResult = service.addSessionToCollectionIfMissing([], session, session2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(session);
        expect(expectedResult).toContain(session2);
      });

      it('should accept null and undefined values', () => {
        const session: ISession = { id: 'ABC' };
        expectedResult = service.addSessionToCollectionIfMissing([], null, session, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(session);
      });

      it('should return initial array if no Session is added', () => {
        const sessionCollection: ISession[] = [{ id: 'ABC' }];
        expectedResult = service.addSessionToCollectionIfMissing(sessionCollection, undefined, null);
        expect(expectedResult).toEqual(sessionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
