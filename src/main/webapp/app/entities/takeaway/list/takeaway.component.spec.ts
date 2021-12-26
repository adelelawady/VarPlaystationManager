import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TakeawayService } from '../service/takeaway.service';

import { TakeawayComponent } from './takeaway.component';

describe('Takeaway Management Component', () => {
  let comp: TakeawayComponent;
  let fixture: ComponentFixture<TakeawayComponent>;
  let service: TakeawayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TakeawayComponent],
    })
      .overrideTemplate(TakeawayComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TakeawayComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TakeawayService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.takeaways?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
