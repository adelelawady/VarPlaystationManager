jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TakeawayService } from '../service/takeaway.service';
import { ITakeaway, Takeaway } from '../takeaway.model';

import { TakeawayUpdateComponent } from './takeaway-update.component';

describe('Takeaway Management Update Component', () => {
  let comp: TakeawayUpdateComponent;
  let fixture: ComponentFixture<TakeawayUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let takeawayService: TakeawayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TakeawayUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TakeawayUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TakeawayUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    takeawayService = TestBed.inject(TakeawayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const takeaway: ITakeaway = { id: 'CBA' };

      activatedRoute.data = of({ takeaway });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(takeaway));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Takeaway>>();
      const takeaway = { id: 'ABC' };
      jest.spyOn(takeawayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ takeaway });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: takeaway }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(takeawayService.update).toHaveBeenCalledWith(takeaway);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Takeaway>>();
      const takeaway = new Takeaway();
      jest.spyOn(takeawayService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ takeaway });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: takeaway }));
      saveSubject.complete();

      // THEN
      expect(takeawayService.create).toHaveBeenCalledWith(takeaway);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Takeaway>>();
      const takeaway = { id: 'ABC' };
      jest.spyOn(takeawayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ takeaway });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(takeawayService.update).toHaveBeenCalledWith(takeaway);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
