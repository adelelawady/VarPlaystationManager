jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TableService } from '../service/table.service';
import { ITable, Table } from '../table.model';

import { TableUpdateComponent } from './table-update.component';

describe('Table Management Update Component', () => {
  let comp: TableUpdateComponent;
  let fixture: ComponentFixture<TableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tableService: TableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TableUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tableService = TestBed.inject(TableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const table: ITable = { id: 'CBA' };

      activatedRoute.data = of({ table });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(table));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Table>>();
      const table = { id: 'ABC' };
      jest.spyOn(tableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ table });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: table }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tableService.update).toHaveBeenCalledWith(table);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Table>>();
      const table = new Table();
      jest.spyOn(tableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ table });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: table }));
      saveSubject.complete();

      // THEN
      expect(tableService.create).toHaveBeenCalledWith(table);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Table>>();
      const table = { id: 'ABC' };
      jest.spyOn(tableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ table });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tableService.update).toHaveBeenCalledWith(table);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
