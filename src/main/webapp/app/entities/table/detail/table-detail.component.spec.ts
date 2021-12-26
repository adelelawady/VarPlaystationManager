import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TableDetailComponent } from './table-detail.component';

describe('Table Management Detail Component', () => {
  let comp: TableDetailComponent;
  let fixture: ComponentFixture<TableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ table: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(TableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load table on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.table).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
