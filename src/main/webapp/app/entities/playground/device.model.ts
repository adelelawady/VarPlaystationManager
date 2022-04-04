export interface IDevice {
  id?: string;
  name?: string | null;
}

export class Device implements IDevice {
  constructor(public id?: string, public name?: string | null) {}
}

export function getDeviceIdentifier(device: IDevice): string | undefined {
  return device.id;
}
