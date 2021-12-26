import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TakeawayDetailComponent } from './takeaway-detail.component';

describe('Takeaway Management Detail Component', () => {
  let comp: TakeawayDetailComponent;
  let fixture: ComponentFixture<TakeawayDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TakeawayDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ takeaway: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(TakeawayDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TakeawayDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load takeaway on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.takeaway).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
