import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SessionDetailComponent } from './session-detail.component';

describe('Session Management Detail Component', () => {
  let comp: SessionDetailComponent;
  let fixture: ComponentFixture<SessionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SessionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ session: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(SessionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SessionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load session on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.session).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
