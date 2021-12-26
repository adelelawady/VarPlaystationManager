import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShopsOrdersDetailComponent } from './shops-orders-detail.component';

describe('ShopsOrders Management Detail Component', () => {
  let comp: ShopsOrdersDetailComponent;
  let fixture: ComponentFixture<ShopsOrdersDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShopsOrdersDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ shopsOrders: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ShopsOrdersDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ShopsOrdersDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load shopsOrders on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.shopsOrders).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
