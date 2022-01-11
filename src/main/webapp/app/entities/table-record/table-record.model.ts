export interface ITableRecord {
  id?: string;
  totalPrice?: number | null;
  totalDiscountPrice?: number | null;
  netTotalPrice?: number | null;
  discount?: number | null;
}

export class TableRecord implements ITableRecord {
  constructor(
    public id?: string,
    public totalPrice?: number | null,
    public totalDiscountPrice?: number | null,
    public netTotalPrice?: number | null,
    public discount?: number | null
  ) {}
}

export function getTableRecordIdentifier(tableRecord: ITableRecord): string | undefined {
  return tableRecord.id;
}
