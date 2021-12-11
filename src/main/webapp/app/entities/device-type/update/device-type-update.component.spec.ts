jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeviceTypeService } from '../service/device-type.service';
import { IDeviceType, DeviceType } from '../device-type.model';

import { DeviceTypeUpdateComponent } from './device-type-update.component';

describe('DeviceType Management Update Component', () => {
  let comp: DeviceTypeUpdateComponent;
  let fixture: ComponentFixture<DeviceTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deviceTypeService: DeviceTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DeviceTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DeviceTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeviceTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deviceTypeService = TestBed.inject(DeviceTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const deviceType: IDeviceType = { id: 'CBA' };

      activatedRoute.data = of({ deviceType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deviceType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeviceType>>();
      const deviceType = { id: 'ABC' };
      jest.spyOn(deviceTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deviceTypeService.update).toHaveBeenCalledWith(deviceType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeviceType>>();
      const deviceType = new DeviceType();
      jest.spyOn(deviceTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deviceType }));
      saveSubject.complete();

      // THEN
      expect(deviceTypeService.create).toHaveBeenCalledWith(deviceType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DeviceType>>();
      const deviceType = { id: 'ABC' };
      jest.spyOn(deviceTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deviceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deviceTypeService.update).toHaveBeenCalledWith(deviceType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
