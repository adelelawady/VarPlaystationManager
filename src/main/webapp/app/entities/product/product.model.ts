import { ICategory } from 'app/entities/category/category.model';
import { ISession } from 'app/entities/session/session.model';
import { IRecord } from 'app/entities/record/record.model';

export interface IProduct {
  id?: string;
  name?: string | null;
  price?: number | null;
  category?: ICategory | null;
  sessions?: ISession[] | null;
  records?: IRecord[] | null;
}

export class Product implements IProduct {
  constructor(
    public id?: string,
    public name?: string | null,
    public price?: number | null,
    public category?: ICategory | null,
    public sessions?: ISession[] | null,
    public records?: IRecord[] | null
  ) {}
}

export function getProductIdentifier(product: IProduct): string | undefined {
  return product.id;
}
