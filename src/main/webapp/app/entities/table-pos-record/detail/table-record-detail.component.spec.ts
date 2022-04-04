import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TableRecordDetailComponent } from './table-record-detail.component';

describe('TableRecord Management Detail Component', () => {
  let comp: TableRecordDetailComponent;
  let fixture: ComponentFixture<TableRecordDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TableRecordDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tableRecord: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(TableRecordDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TableRecordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tableRecord on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tableRecord).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
