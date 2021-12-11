import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeviceTypeDetailComponent } from './device-type-detail.component';

describe('DeviceType Management Detail Component', () => {
  let comp: DeviceTypeDetailComponent;
  let fixture: ComponentFixture<DeviceTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeviceTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deviceType: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(DeviceTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeviceTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deviceType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deviceType).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
