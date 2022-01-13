import * as dayjs from 'dayjs';

export interface ISheft {
  id?: string;
  user?: string | null;
  start?: dayjs.Dayjs | null;
  end?: dayjs.Dayjs | null;
  totalNetPrice?: number | null;
  totalDiscount?: number | null;
  totalNetPriceAfterDiscount?: number | null;
  totalNetPriceAfterDiscountSystem?: number | null;
  totalNetPriceDevices?: number | null;
  totalNetUserPriceDevices?: number | null;
  totalDiscountPriceDevices?: number | null;
  totalPriceTimeDevices?: number | null;
  totalPriceOrdersDevices?: number | null;
  totalNetPriceTables?: number | null;
  totalDiscountPriceTables?: number | null;
  totalNetPriceAfterDiscountTables?: number | null;
  totalNetPriceTakeaway?: number | null;
  totalDiscountPriceTakeaway?: number | null;
  totalNetPriceAfterDiscountTakeaway?: number | null;
  totalNetPriceShops?: number | null;
  totalDiscountPriceShops?: number | null;
  totalNetPriceAfterDiscountShops?: number | null;
}

export class Sheft implements ISheft {
  constructor(
    public id?: string,
    public user?: string | null,
    public start?: dayjs.Dayjs | null,
    public end?: dayjs.Dayjs | null,
    public totalNetPrice?: number | null,
    public totalDiscount?: number | null,
    public totalNetPriceAfterDiscount?: number | null,
    public totalNetPriceAfterDiscountSystem?: number | null,
    public totalNetPriceDevices?: number | null,
    public totalNetUserPriceDevices?: number | null,
    public totalDiscountPriceDevices?: number | null,
    public totalPriceTimeDevices?: number | null,
    public totalPriceOrdersDevices?: number | null,
    public totalNetPriceTables?: number | null,
    public totalDiscountPriceTables?: number | null,
    public totalNetPriceAfterDiscountTables?: number | null,
    public totalNetPriceTakeaway?: number | null,
    public totalDiscountPriceTakeaway?: number | null,
    public totalNetPriceAfterDiscountTakeaway?: number | null,
    public totalNetPriceShops?: number | null,
    public totalDiscountPriceShops?: number | null,
    public totalNetPriceAfterDiscountShops?: number | null
  ) {}
}

export function getSheftIdentifier(sheft: ISheft): string | undefined {
  return sheft.id;
}
