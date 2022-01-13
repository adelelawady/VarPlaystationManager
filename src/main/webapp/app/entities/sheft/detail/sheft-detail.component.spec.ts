import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SheftDetailComponent } from './sheft-detail.component';

describe('Sheft Management Detail Component', () => {
  let comp: SheftDetailComponent;
  let fixture: ComponentFixture<SheftDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SheftDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sheft: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(SheftDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SheftDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sheft on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sheft).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
