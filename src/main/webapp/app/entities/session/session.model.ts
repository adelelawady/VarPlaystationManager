import * as dayjs from 'dayjs';
import { IDevice } from 'app/entities/device/device.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ISession {
  id?: string;
  start?: dayjs.Dayjs | null;
  reserved?: number | null;
  device?: IDevice;
  orders?: IProduct[] | null;
}

export class Session implements ISession {
  constructor(
    public id?: string,
    public start?: dayjs.Dayjs | null,
    public reserved?: number | null,
    public device?: IDevice,
    public orders?: IProduct[] | null
  ) {}
}

export function getSessionIdentifier(session: ISession): string | undefined {
  return session.id;
}
