import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeviceDetailComponent } from './device-detail.component';

describe('Device Management Detail Component', () => {
  let comp: DeviceDetailComponent;
  let fixture: ComponentFixture<DeviceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeviceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ device: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(DeviceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeviceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load device on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.device).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
