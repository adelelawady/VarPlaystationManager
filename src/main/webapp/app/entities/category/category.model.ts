export interface ICategory {
  id?: string;
  name?: string | null;
}

export class Category implements ICategory {
  constructor(public id?: string, public name?: string | null) {}
}

export function getCategoryIdentifier(category: ICategory): string | undefined {
  return category.id;
}
