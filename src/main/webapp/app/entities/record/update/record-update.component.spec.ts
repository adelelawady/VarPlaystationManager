jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RecordService } from '../service/record.service';
import { IRecord, Record } from '../record.model';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { RecordUpdateComponent } from './record-update.component';

describe('Record Management Update Component', () => {
  let comp: RecordUpdateComponent;
  let fixture: ComponentFixture<RecordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recordService: RecordService;
  let deviceService: DeviceService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RecordUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RecordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recordService = TestBed.inject(RecordService);
    deviceService = TestBed.inject(DeviceService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call device query and add missing value', () => {
      const record: IRecord = { id: 'CBA' };
      const device: IDevice = { id: '84606075-f20a-4ca8-8dc7-63041f8d32f0' };
      record.device = device;

      const deviceCollection: IDevice[] = [{ id: '11803e7b-ac82-4f40-81fe-0e3eb90b8600' }];
      jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
      const expectedCollection: IDevice[] = [device, ...deviceCollection];
      jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ record });
      comp.ngOnInit();

      expect(deviceService.query).toHaveBeenCalled();
      expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(deviceCollection, device);
      expect(comp.devicesCollection).toEqual(expectedCollection);
    });

    it('Should call Product query and add missing value', () => {
      const record: IRecord = { id: 'CBA' };
      const orders: IProduct[] = [{ id: '61ba5c99-4823-40b0-9340-69d59cd655de' }];
      record.orders = orders;

      const productCollection: IProduct[] = [{ id: '854e1962-0aa2-47c3-8ea4-09ba5677470b' }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [...orders];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ record });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const record: IRecord = { id: 'CBA' };
      const device: IDevice = { id: '076aab8f-3c55-4579-9e1a-f5deae0c4dee' };
      record.device = device;
      const orders: IProduct = { id: '4d62e8b5-b4c4-471d-93f1-5f80664f1de3' };
      record.orders = [orders];

      activatedRoute.data = of({ record });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(record));
      expect(comp.devicesCollection).toContain(device);
      expect(comp.productsSharedCollection).toContain(orders);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Record>>();
      const record = { id: 'ABC' };
      jest.spyOn(recordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ record });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: record }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(recordService.update).toHaveBeenCalledWith(record);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Record>>();
      const record = new Record();
      jest.spyOn(recordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ record });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: record }));
      saveSubject.complete();

      // THEN
      expect(recordService.create).toHaveBeenCalledWith(record);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Record>>();
      const record = { id: 'ABC' };
      jest.spyOn(recordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ record });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recordService.update).toHaveBeenCalledWith(record);
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
