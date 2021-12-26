import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TableService } from '../service/table.service';

import { TableComponent } from './table.component';

describe('Table Management Component', () => {
  let comp: TableComponent;
  let fixture: ComponentFixture<TableComponent>;
  let service: TableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TableComponent],
    })
      .overrideTemplate(TableComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TableComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TableService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.tables?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
