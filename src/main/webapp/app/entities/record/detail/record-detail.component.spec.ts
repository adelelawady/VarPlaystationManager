import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RecordDetailComponent } from './record-detail.component';

describe('Record Management Detail Component', () => {
  let comp: RecordDetailComponent;
  let fixture: ComponentFixture<RecordDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecordDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ record: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(RecordDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RecordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load record on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.record).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
