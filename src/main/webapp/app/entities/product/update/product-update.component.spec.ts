jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProductService } from '../service/product.service';
import { IProduct, Product } from '../product.model';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productService: ProductService;
  let categoryService: CategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProductUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productService = TestBed.inject(ProductService);
    categoryService = TestBed.inject(CategoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call category query and add missing value', () => {
      const product: IProduct = { id: 'CBA' };
      const category: ICategory = { id: '8a59da62-356e-4881-9eee-0585187841f6' };
      product.category = category;

      const categoryCollection: ICategory[] = [{ id: 'e3291de2-b54b-44d6-8197-30b94f41cb01' }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const expectedCollection: ICategory[] = [category, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, category);
      expect(comp.categoriesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 'CBA' };
      const category: ICategory = { id: '5a1c5380-25f7-405b-b53c-c133744fe0cd' };
      product.category = category;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(product));
      expect(comp.categoriesCollection).toContain(category);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Product>>();
      const product = { id: 'ABC' };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(product);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Product>>();
      const product = new Product();
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productService.create).toHaveBeenCalledWith(product);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Product>>();
      const product = { id: 'ABC' };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalledWith(product);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCategoryById', () => {
      it('Should return tracked Category primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
