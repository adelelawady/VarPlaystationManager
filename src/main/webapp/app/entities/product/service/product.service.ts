import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProduct, getProductIdentifier } from '../product.model';

export type EntityResponseType = HttpResponse<IProduct>;
export type EntityArrayResponseType = HttpResponse<IProduct[]>;

@Injectable({ providedIn: 'root' })
export class ProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(product: IProduct): Observable<EntityResponseType> {
    return this.http.post<IProduct>(this.resourceUrl, product, { observe: 'response' });
  }

  translateProducts(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/translate-products`, { observe: 'response' });
  }
  update(product: IProduct): Observable<EntityResponseType> {
    return this.http.put<IProduct>(`${this.resourceUrl}/${getProductIdentifier(product) as string}`, product, { observe: 'response' });
  }

  partialUpdate(product: IProduct): Observable<EntityResponseType> {
    return this.http.patch<IProduct>(`${this.resourceUrl}/${getProductIdentifier(product) as string}`, product, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryByCategoryId(categoryId: string): Observable<EntityArrayResponseType> {
    return this.http.get<IProduct[]>(`${this.resourceUrl}/category/${categoryId}`, { observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSubItem(prodId: string, item: any): Observable<EntityArrayResponseType> {
    return this.http.post<IProduct[]>(`${this.resourceUrl}/${prodId}/addsub`, item, { observe: 'response' });
  }
  removeSubItem(prodId: string, item: string): Observable<EntityArrayResponseType> {
    return this.http.get<IProduct[]>(`${this.resourceUrl}/${prodId}/removesub/${item}`, { observe: 'response' });
  }

  //products/{productId}/addsub

  addProductToCollectionIfMissing(productCollection: IProduct[], ...productsToCheck: (IProduct | null | undefined)[]): IProduct[] {
    const products: IProduct[] = productsToCheck.filter(isPresent);
    if (products.length > 0) {
      const productCollectionIdentifiers = productCollection.map(productItem => getProductIdentifier(productItem)!);
      const productsToAdd = products.filter(productItem => {
        const productIdentifier = getProductIdentifier(productItem);
        if (productIdentifier == null || productCollectionIdentifiers.includes(productIdentifier)) {
          return false;
        }
        productCollectionIdentifiers.push(productIdentifier);
        return true;
      });
      return [...productsToAdd, ...productCollection];
    }
    return productCollection;
  }
}
