import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategoryDetailComponent } from './category-detail.component';

describe('Category Management Detail Component', () => {
  let comp: CategoryDetailComponent;
  let fixture: ComponentFixture<CategoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CategoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ category: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(CategoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load category on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.category).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
