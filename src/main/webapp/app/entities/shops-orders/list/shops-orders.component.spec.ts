import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ShopsOrdersService } from '../service/shops-orders.service';

import { ShopsOrdersComponent } from './shops-orders.component';

describe('ShopsOrders Management Component', () => {
  let comp: ShopsOrdersComponent;
  let fixture: ComponentFixture<ShopsOrdersComponent>;
  let service: ShopsOrdersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ShopsOrdersComponent],
    })
      .overrideTemplate(ShopsOrdersComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShopsOrdersComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ShopsOrdersService);

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
    expect(comp.shopsOrders?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
