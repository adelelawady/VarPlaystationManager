/* eslint-disable @typescript-eslint/no-unsafe-return */
import { Pipe, PipeTransform } from '@angular/core';
import { interval } from 'rxjs/internal/observable/interval';
import { map } from 'rxjs/operators';

@Pipe({
  name: 'devicePrice',
})
export class DevicePricePipe implements PipeTransform {
  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  transform(device: any, int: boolean) {
    return interval(10).pipe(
      map(() => {
        let diff = (new Date(device.session.start).getTime() - new Date().getTime()) / 1000;
        diff /= 60;
        const diffMin = Math.abs(Math.round(diff));
        // eslint-disable-next-line @typescript-eslint/restrict-plus-operands

        let pricePerHour = device.session.device.type.pricePerHour;
        if (device.session.multi) {
          pricePerHour = device.session.device.type.pricePerHourMulti;
        } else {
          pricePerHour = device.session.device.type.pricePerHour;
        }

        const totalOrderprice = device.session.ordersPrice ? device.session.ordersPrice : 0;
        if (int) {
          // eslint-disable-next-line @typescript-eslint/no-unsafe-return
          // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
          return Math.round((diffMin / 60) * pricePerHour) + totalOrderprice;
        } else {
          // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
          return Math.round((diffMin / 60) * pricePerHour) + totalOrderprice + ' LE';
        }
      })
    );
  }
}
