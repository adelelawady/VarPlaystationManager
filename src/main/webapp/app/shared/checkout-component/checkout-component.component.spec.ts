import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckoutComponentComponent } from './checkout-component.component';

describe('CheckoutComponentComponent', () => {
  let component: CheckoutComponentComponent;
  let fixture: ComponentFixture<CheckoutComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CheckoutComponentComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckoutComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
