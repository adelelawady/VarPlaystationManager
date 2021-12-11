jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SessionService } from '../service/session.service';
import { ISession, Session } from '../session.model';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { SessionUpdateComponent } from './session-update.component';

describe('Session Management Update Component', () => {
  let comp: SessionUpdateComponent;
  let fixture: ComponentFixture<SessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sessionService: SessionService;
  let deviceService: DeviceService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SessionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionService = TestBed.inject(SessionService);
    deviceService = TestBed.inject(DeviceService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call device query and add missing value', () => {
      const session: ISession = { id: 'CBA' };
      const device: IDevice = { id: '0c00fc5b-4332-4c2d-8423-eba3f94e1911' };
      session.device = device;

      const deviceCollection: IDevice[] = [{ id: '22684a08-50b8-49b5-b879-60b387664db7' }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const expectedCollection: IDevice[] = [device, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ session });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(deviceCollection, device);
      expect(comp.devicesCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const session: ISession = { id: 'CBA' };
      const orders: IProduct[] = [{ id: 'c7587eca-e693-4939-ae80-6703899e1c2d' }];
      session.orders = orders;

      const productCollection: IProduct[] = [{ id: '318c328d-1c18-4e2c-9846-c11daca8d79f' }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [...orders];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ session });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const session: ISession = { id: 'CBA' };
      const device: IDevice = { id: 'da350c3b-8fe3-4152-bcfd-49dadcd273c9' };
      session.device = device;
      const orders: IProduct = { id: '5baadbe1-dabd-4539-a093-59abe4e0ffad' };
      session.orders = [orders];

      activatedRoute.data = of({ session });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(session));
      expect(comp.devicesCollection).toContain(device);
      expect(comp.productsSharedCollection).toContain(orders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Session>>();
      const session = { id: 'ABC' };
      jest.spyOn(sessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ session });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: session }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sessionService.update).toHaveBeenCalledWith(session);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Session>>();
      const session = new Session();
      jest.spyOn(sessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ session });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: session }));
      saveSubject.complete();

      // THEN
      expect(sessionService.create).toHaveBeenCalledWith(session);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Session>>();
      const session = { id: 'ABC' };
      jest.spyOn(sessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ session });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sessionService.update).toHaveBeenCalledWith(session);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDeviceById', () => {
      it('Should return tracked Device primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackDeviceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProductById', () => {
      it('Should return tracked Product primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedProduct', () => {
      it('Should return option if no Product is selected', () => {
        const option = { id: 'ABC' };
        const result = comp.getSelectedProduct(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Product for according option', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'ABC' };
        const selected2 = { id: 'CBA' };
        const result = comp.getSelectedProduct(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Product is not selected', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'CBA' };
        const result = comp.getSelectedProduct(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
