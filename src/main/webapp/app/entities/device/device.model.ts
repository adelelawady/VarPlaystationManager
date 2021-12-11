import { IDeviceType } from 'app/entities/device-type/device-type.model';

export interface IDevice {
  id?: string;
  name?: string | null;
  type?: IDeviceType;
}

export class Device implements IDevice {
  constructor(public id?: string, public name?: string | null, public type?: IDeviceType) {}
}

export function getDeviceIdentifier(device: IDevice): string | undefined {
  return device.id;
}
