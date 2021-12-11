jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeviceService } from '../service/device.service';
import { IDevice, Device } from '../device.model';
import { IDeviceType } from 'app/entities/device-type/device-type.model';
import { DeviceTypeService } from 'app/entities/device-type/service/device-type.service';

import { DeviceUpdateComponent } from './device-update.component';

describe('Device Management Update Component', () => {
  let comp: DeviceUpdateComponent;
  let fixture: ComponentFixture<DeviceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deviceService: DeviceService;
  let deviceTypeService: DeviceTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DeviceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DeviceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deviceService = TestBed.inject(DeviceService);
    deviceTypeService = TestBed.inject(DeviceTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call type query and add missing value', () => {
      const device: IDevice = { id: 'CBA' };
      const type: IDeviceType = { id: '370498f3-1996-4284-80f8-4da40c576de8' };
      device.type = type;

      const typeCollection: IDeviceType[] = [{ id: 'd4a99353-0eec-4ad3-9c9e-e5ad96ba3081' }];
      jest.spyOn(deviceTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: typeCollection })));
      const expectedCollection: IDeviceType[] = [type, ...typeCollection];
      jest.spyOn(deviceTypeService, 'addDeviceTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(deviceTypeService.query).toHaveBeenCalled();
      expect(deviceTypeService.addDeviceTypeToCollectionIfMissing).toHaveBeenCalledWith(typeCollection, type);
      expect(comp.typesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const device: IDevice = { id: 'CBA' };
      const type: IDeviceType = { id: 'c2186cd4-fdb0-4843-bb25-14b718308964' };
      device.type = type;

      activatedRoute.data = of({ device });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(device));
      expect(comp.typesCollection).toContain(type);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Device>>();
      const device = { id: 'ABC' };
      jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: device }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deviceService.update).toHaveBeenCalledWith(device);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Device>>();
      const device = new Device();
      jest.spyOn(deviceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: device }));
      saveSubject.complete();

      // THEN
      expect(deviceService.create).toHaveBeenCalledWith(device);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Device>>();
      const device = { id: 'ABC' };
      jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ device });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deviceService.update).toHaveBeenCalledWith(device);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDeviceTypeById', () => {
      it('Should return tracked DeviceType primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackDeviceTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
