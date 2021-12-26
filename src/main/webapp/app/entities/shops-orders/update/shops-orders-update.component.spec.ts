jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ShopsOrdersService } from '../service/shops-orders.service';
import { IShopsOrders, ShopsOrders } from '../shops-orders.model';

import { ShopsOrdersUpdateComponent } from './shops-orders-update.component';

describe('ShopsOrders Management Update Component', () => {
  let comp: ShopsOrdersUpdateComponent;
  let fixture: ComponentFixture<ShopsOrdersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shopsOrdersService: ShopsOrdersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ShopsOrdersUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ShopsOrdersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShopsOrdersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shopsOrdersService = TestBed.inject(ShopsOrdersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const shopsOrders: IShopsOrders = { id: 'CBA' };

      activatedRoute.data = of({ shopsOrders });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(shopsOrders));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShopsOrders>>();
      const shopsOrders = { id: 'ABC' };
      jest.spyOn(shopsOrdersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shopsOrders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shopsOrders }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(shopsOrdersService.update).toHaveBeenCalledWith(shopsOrders);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShopsOrders>>();
      const shopsOrders = new ShopsOrders();
      jest.spyOn(shopsOrdersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shopsOrders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shopsOrders }));
      saveSubject.complete();

      // THEN
      expect(shopsOrdersService.create).toHaveBeenCalledWith(shopsOrders);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShopsOrders>>();
      const shopsOrders = { id: 'ABC' };
      jest.spyOn(shopsOrdersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shopsOrders });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shopsOrdersService.update).toHaveBeenCalledWith(shopsOrders);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
