export interface ITable {
  id?: string;
  name?: string | null;
  discount?: number | null;
  totalPrice?: number | null;
}

export class Table implements ITable {
  constructor(public id?: string, public name?: string | null, public discount?: number | null, public totalPrice?: number | null) {}
}

export function getTableIdentifier(table: ITable): string | undefined {
  return table.id;
}
