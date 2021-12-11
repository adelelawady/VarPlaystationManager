import * as dayjs from 'dayjs';
import { IDevice } from 'app/entities/device/device.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IRecord {
  id?: string;
  start?: dayjs.Dayjs | null;
  end?: dayjs.Dayjs | null;
  totalPrice?: number | null;
  device?: IDevice;
  orders?: IProduct[] | null;
}

export class Record implements IRecord {
  constructor(
    public id?: string,
    public start?: dayjs.Dayjs | null,
    public end?: dayjs.Dayjs | null,
    public totalPrice?: number | null,
    public device?: IDevice,
    public orders?: IProduct[] | null
  ) {}
}

export function getRecordIdentifier(record: IRecord): string | undefined {
  return record.id;
}
