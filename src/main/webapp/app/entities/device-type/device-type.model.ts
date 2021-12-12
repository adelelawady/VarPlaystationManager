export interface IDeviceType {
  id?: string;
  name?: string | null;
  pricePerHour?: number | null;
  pricePerHourMulti?: number | null;
}

export class DeviceType implements IDeviceType {
  constructor(public id?: string, public name?: string | null, public pricePerHour?: number | null) {}
}

export function getDeviceTypeIdentifier(deviceType: IDeviceType): string | undefined {
  return deviceType.id;
}
