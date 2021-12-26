export interface IShopsOrders {
  id?: string;
  name?: string | null;
  totalPrice?: number | null;
}

export class ShopsOrders implements IShopsOrders {
  constructor(public id?: string, public name?: string | null, public totalPrice?: number | null) {}
}

export function getShopsOrdersIdentifier(shopsOrders: IShopsOrders): string | undefined {
  return shopsOrders.id;
}
