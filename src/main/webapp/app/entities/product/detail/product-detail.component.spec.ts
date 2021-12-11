import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductDetailComponent } from './product-detail.component';

describe('Product Management Detail Component', () => {
  let comp: ProductDetailComponent;
  let fixture: ComponentFixture<ProductDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ product: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ProductDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load product on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.product).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
